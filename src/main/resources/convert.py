#!/usr/bin/python3

from sys    import argv, stderr, stdout
from json   import load as load_json
from re     import sub, match, compile as re_compile

if len(argv) < 2:
    print("Usage: {} <mappings file> [<output file> [<error file]]".format(argv[0]))
    exit()

# note: `names` maps $(item) names to entry names; `paths` maps entry names to link names

def resolve_file(index, mode, default=None):
    return open(argv[index], mode) if len(argv) > index else default

with resolve_file(1, "r") as fin:
    data = load_json(fin)
unknowns = set() # so they're buffered all after stdout
errors = []

item_pattern = re_compile(r"(?<!\))\$\(item\)([^$]+)\$\([0r]\)(?!\$)")

entry_pat = re_compile(r"#(\w+)(\+?)(?: from:(\w+))?(?: icon:(\w+))? (.*)")

with open(data["file"], "r") as fin, \
        resolve_file(2, "w", stdout) as out:
    for line in fin:
        line = line[:-1] # strip newline
        if not line.strip():
            print(line, file=out)
            continue
        matcher = entry_pat.fullmatch(line)
        if matcher:
            suffix = matcher.group(1)
        def item_cb(matcher):
            whole = matcher.group(0)
            phrase = matcher.group(1)
            if phrase in data["ignore"]: return whole
            if phrase not in data["names"]:
                unknowns.add('    "{}",'.format(phrase))
                return whole
            if match("/{}(#|\))".format(suffix), data["names"][phrase]):
                return whole
            return "$(l:{})$(item){}$(0)$(/l)".format(data["names"][phrase], phrase)
        print(sub(item_pattern, item_cb, line), file=out)
with resolve_file(3, "w", stderr) as err:
    if unknowns:
        print("===UNKNOWN ITEMS:===", file=err)
        for warn in sorted(unknowns):
            print(warn, file=err)
    if errors:
        print("===ERRORS:===", file=err)
        for bad in sorted(errors):
            print(bad, file=err)
