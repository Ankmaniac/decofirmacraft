import os
import shutil

source_dir = "D:/Modding/decofirmacraft/decofirmacraft 1.21/src/datagen/placeholder/rock1"
output_dir = "D:/Modding/decofirmacraft/decofirmacraft 1.21/src/datagen/placeholder/rock"
replacements = ["brown_sandstone", "white_sandstone", "black_sandstone", "red_sandstone", "yellow_sandstone", "green_sandstone", "pink_sandstone", "tuff", "flint", "ignimbrite", "obsidian", "peridotite", "pumice", "serpentine", "soapstone"]

target_word = "serpentine"

for root, dirs, files in os.walk(source_dir):
    for file in files:
        if target_word in file:
            src_path = os.path.join(root, file)

            with open(src_path, "r", encoding="utf-8") as f:
                content = f.read()

            for rock in replacements:
                new_filename = file.replace(target_word, rock)
                new_content = content.replace(target_word, rock)

                rel_path = os.path.relpath(root, source_dir)
                dest_dir = os.path.join(output_dir, rel_path)
                os.makedirs(dest_dir, exist_ok=True)

                dest_path = os.path.join(dest_dir, new_filename)

                with open(dest_path, "w", encoding="utf-8") as f:
                    f.write(new_content)