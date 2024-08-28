from mcresources import ResourceManager, utils
from mcresources.type_definitions import ResourceIdentifier, JsonObject

import format_lang
from argparse import ArgumentParser
from typing import Optional

from i18n import I18n


class LocalInstance:
    INSTANCE_DIR = None

    @staticmethod
    def wrap(rm: ResourceManager):
        def data(name_parts: ResourceIdentifier, data_in: JsonObject, root_domain: str = 'data'):
            return rm.write((LocalInstance.INSTANCE_DIR, '/'.join(utils.str_path(name_parts))), data_in)

        if LocalInstance.INSTANCE_DIR is not None:
            rm.data = data
            return rm
        return None
def main_with_args():
    parser = ArgumentParser('generate_book.py')
    parser.add_argument('--translate', type=str, default='en_us', help='The language to translate to')
    parser.add_argument('--local', type=str, default=None, help='The directory of a local .minecraft to copy into')
    parser.add_argument('--translate-all', type=str, default=None, help='If all languages should be translated')
    parser.add_argument('--format', type=str, default=None, help='Format the mod languages')
    parser.add_argument('--reverse-translate', type=str, default=None, help='Reverse a translation from the mod files.')

    args = parser.parse_args()

    if args.format:
        do_format()
        return






def do_format():
    # format_lang.main(False, 'minecraft', BOOK_LANGUAGES)
    format_lang.main(True, 'dfc', 'en_us')

if __name__ == '__main__':
    main_with_args()