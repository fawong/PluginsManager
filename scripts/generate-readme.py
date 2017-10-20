#!/usr/bin/python

import sys
import yaml
import argparse
import waflibs
import json
from jinja2 import Template

def format_readme(yaml_dict):
    return "```json\n{}\n```".format(json.dumps(yaml_dict, sort_keys=True))

parser = argparse.ArgumentParser()
waflibs.arg_parse.enable_verbose_logging(parser)
parser.add_argument("-t", "--template-file", help="template file", type=str)
parser.add_argument("-p", "--plugin-file", help="plugin file", type=str)

args = parser.parse_args()

logger = waflibs.log.create_logger(args)

logger.debug("args: %s" % args)

template_file = args.template_file

if not (template_file and args.plugin_file):
    message = "you must specify a template file and plugin file"
    logger.error(message)

    parser.print_help()

    raise Exception(message)

plugin_config_contents = yaml.load(open(args.plugin_file))
logger.debug(plugin_config_contents)

commands = format_readme(plugin_config_contents["commands"])
permissions = format_readme(plugin_config_contents["permissions"])

template_contents = open(template_file).read()
logger.debug(template_contents)

template = Template(template_contents)
rendered_file = template.render(description=plugin_config_contents["description"],
                                version=plugin_config_contents["version"],
                                commands=commands,
                                permissions=permissions
                               )

f = open(".".join(template_file.split(".")[0:-1]), "w")
f.write(rendered_file)
f.write("\n")
f.close()
