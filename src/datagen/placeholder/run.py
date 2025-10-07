rocks = [
    "GRANITE",
    "DIORITE",
    "GABBRO",
    "SHALE",
    "CLAYSTONE",
    "LIMESTONE",
    "CONGLOMERATE",
    "DOLOMITE",
    "CHERT",
    "CHALK",
    "TUFF",
    "RHYOLITE",
    "BASALT",
    "ANDESITE",
    "DACITE",
    "QUARTZITE",
    "SLATE",
    "PHYLLITE",
    "SCHIST",
    "GNEISS",
    "MARBLE",
    "ARKOSE",
    "BLUESCHIST",
    "FLINT",
    "OBSIDIAN",
    "PERIDOTITE",
    "SERPENTINE",
    "SOAPSTONE",
    "TRAVERTINE"
]

# Load the marble blockstate file
with open("D:/Modding/decofirmacraft 1.21/src/main/resources/assets/dfc/blockstates/rock/rail/marble.json", "r") as f:
    base_data = f.read()

# Loop through all rocks and create new files
for rock in rocks:
    rock_lower = rock.lower()
    new_data = base_data.replace("marble", rock_lower)
    out_file = f"blockstate_{rock_lower}.json"
    with open(out_file, "w") as f:
        f.write(new_data)
    print(f"Created {out_file}")
