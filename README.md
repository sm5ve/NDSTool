This tool allows one to explore the file structure of a Nintendo DS ROM and view some of the files. Currently, partial support is implemented for

* NARC Nitro Archives
* SMDL Sequenced Music from Pokemon Mystery Dungeon Explorers of Darkness/Time/Sky and maybe some other games
* PX compressed files from the PMD 2 including
  * BGP Background graphics
  * Nothing else yet

This tool doesn't have a particularly clear direction yet, but some other functionality includes

* A built in hex viewer
* A pseudo-command line interface to browse the file system
* Cartridge icon decoding
* Allows users to extract files/directories onto their host file system

A current to-do list in rough order of most to least important is

* Comments
* Fix SMDL timing issues
* Fix BGP decoding
* Basic file editing
* More modular structure/plugins
* Better GUI
* File signature-based context menus (as opposed to file extensions)
* SDAT decoding
* Search
* String indexing