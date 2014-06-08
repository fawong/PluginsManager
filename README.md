name: PluginsManager
main: com.fawong.PluginsManager.PluginsManager
version: 14.04.25
website: http://www.fawong.com/minecraft
author: FAWONG
description: Various plugin and server related additions
commands:
  lp:
    description: Lists plugins in several different formats
    usage: |
           /lp <display type>
           Example: /lp row
           Example: /lp column
  listplugins:
    aliases: [lp]
    description: alias for /listplugins
    usage: /listplugins
  pluginsmanager:
    aliases: [pmgr]
    description: manage plugins
    usage: /pluginsmanager
  plm:
    description: alias for /pluginsmanager
    usage: /plm
  pmgr:
    description: alias for /pluginsmanager
    usage: /pmgr
permissions:
    pluginsmanager.*:
        description: Gives access to all pluginsmanager commands
        children:
            pluginsmanager.enable: true
