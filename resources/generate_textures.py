from PIL import Image, ImageDraw, ImageEnhance, ImageOps
from PIL.Image import Transpose

import colorsys
from constants import *

path = './src/main/resources/assets/dfc/textures/'
templates = './resources/texture_templates/'


def overlay_image(front_file_dir, back_file_dir, result_dir, mask: str = None):
    foreground = Image.open(front_file_dir + '.png').convert('RGBA')
    background = Image.open(back_file_dir + '.png').convert('RGBA')
    if mask is None:
        mask = foreground
    else:
        mask = Image.open(mask + '.png').convert('L')
    background.paste(foreground, (0, 0), mask)
    background.save(result_dir + '.png')