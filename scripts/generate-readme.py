#!/usr/bin/python

import sys
import yaml
import argparse
import waflibs

parser = argparse.ArgumentParser()
waflibs.arg_parse.enable_verbose_logging(parser)
args = parser.parse_args()

logger = waflibs.log.create_logger(args)

logger.debug("args: %s" % args)
