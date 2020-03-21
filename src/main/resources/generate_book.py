#!/usr/bin/python3

from sys    import argv, stdin, stderr, stdout
from json   import dump as dump_json, load as load_json
from re     import sub, compile as re_compile
from os     import makedirs

lang = "en_us"
modname = "psi"
modpfx = modname + ":"
bookpfx = modname + ".book"
basepath = "data/" + modname + "/patchouli_books/taurus_silver_handbook/" + lang
imagepath = "textures/gui/entries/{}.png"

if len(argv) < 2:
    print("Usage: {} [<input file>] [<base lang file>] [<output file>]".format(argv[0]), file=stderr)
    exit(-11)

# note: `names` maps $(item) names to entry names; `paths` maps entry names to link names

def resolve_file(index, mode, default=None):
    return open(argv[index], mode) if len(argv) > index and argv[index] != "-" else default

lang_file = {}
fin = resolve_file(2, "r")
if fin:
    lang_file = load_json(fin) or {}
    fin.close()

makedirs(basepath + "/entries", exist_ok=True)
makedirs(basepath + "/categories", exist_ok=True)

camelcased = lambda snakecase: sub("_([a-z])", lambda match: match.group(1).upper(), sub("_([0-9])", r"\1", snakecase))

section_pat = re_compile(r"##(\w+) icon:(\w+) (.*)")
entry_pat = re_compile(r"#(\w+)(\+?)(?: from:(\w+))?(?: icon:(\w+))? (.*)")
page_patterns = [
    ("crafting", re_compile(r"@RECIPE\s*\((\w+)\)(?:: (.*))?"),     lambda match: (match.group(2), {"recipe": modpfx + match.group(1)})),
    ("image",    re_compile(r"@IMAGE\s*(B)?\(([\w,]+)\)(?:: (.*))?"),   lambda match: (match.group(3), {"images": [modpfx + imagepath.format(fn) for fn in match.group(2).split(",")], "border": bool(match.group(1))})),
    ("text",     re_compile(".*"),                                  lambda match: (match.group(0), {}))
]

def dump_entry(entry_path, entry_data):
    with open("{}/entries/{}.json".format(basepath, entry_path), "w") as entry_file:
        dump_json(entry_data, entry_file, indent=2)

with resolve_file(1, "r", stdin) as fin, \
        resolve_file(3, "w", stdout) as out:
    section = None
    secnum = 0
    entry = None
    entry_key = None
    entry_data = None
    for line in fin:
        line = line[:-1] # strip newline
        if not line.strip():
            continue
        if line == "__END__":
            break
        matcher = section_pat.fullmatch(line)
        if matcher:
            section, icon, langname = matcher.groups()
            langkey = bookpfx + ".category." + camelcased(section)
            makedirs(basepath + "/entries/" + section, exist_ok=True)
            with open("{}/categories/{}.json".format(basepath, section), "w") as category_file:
                dump_json({"name": langkey, "description": langkey, "icon": modpfx + icon, "sortnum": secnum}, category_file, indent=2)
            lang_file[langkey] = langname
            secnum += 1
            continue
        matcher = entry_pat.fullmatch(line)
        if matcher:
            if entry_data:
                dump_entry(entry, entry_data)
            name, important, gate, icon, langname = matcher.groups()
            if not section:
                print("E: Found entry {} before a section".format(name), file=stderr)
                exit(11)
            entry = section + "/" + name
            entry_key = camelcased(name)
            langkey = bookpfx + ".entry." + entry_key
            entry_data = {"name": langkey, "category": section, "icon": (modpfx + icon) if icon else "minecraft:air"}
            if important: entry_data["priority"] = True
            if gate: entry_data["advancement"] = modpfx + "main/" + icon
            entry_data["pages"] = []
            lang_file[langkey] = langname
            pageidx = 0
            continue
        if not entry:
            print("E: Found pageline before an entry:", line, file=stderr)
            exit(22)
        for page, regex, cb in page_patterns:
            matcher = regex.fullmatch(line)
            if matcher:
                base = {"type": page}
                line, add = cb(matcher)
                if line:
                    langkey = bookpfx + ".page." + entry_key + "." + str(pageidx)
                    pageidx += 1
                    base["text"] = langkey
                    lang_file[langkey] = line
                base.update(add)
                entry_data["pages"].append(base)
                break
        else:
            print("E: Unknown page type for line:", line, file=stderr)
            exit(33)
    if entry_data:
        dump_entry(entry, entry_data)
    dump_json(lang_file, out, indent=2)
