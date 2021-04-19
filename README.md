# CoX Scouter External
Shows additional information for the [Chambers of Xeric](https://oldschool.runescape.wiki/w/Chambers_of_Xeric) raid

## Overview of added features:
### Split layout by floor:
Simply adds a "|" between the floors.<br/>
![floor-break](https://i.gyazo.com/d96430b564c5d4c2a6746842825670a0.png)

### Item Overlay:
Adds a customizable(see example config below) item overlay to the scouter.<br/>
![items](https://i.gyazo.com/636604e6bea39bed4e325da91144c4a6.png)
![scaled-down items](https://i.gyazo.com/e13eea23ee685d216894c1841ef578e2.png)<br/>
If there are too many items to fit in the overlay, the image size is scaled down.<br/>
![item-overlay-config](https://i.gyazo.com/9a16209e188b6db940b387681eba31da.png)

### Room highlighting & scouter hiding:
Adds a customizable(see example config below) list of highlighted rooms. Also adds the option to hide raids that contain any blacklisted rooms, and the option to hide raids missing any highlighted rooms.<br/>
![highlighted room](https://i.gyazo.com/f9c664b772a64e1245a5ee08b17bc087.png)
![hidden scout](https://i.gyazo.com/b4e0be785cff067768ace64972b8739f.png)<br/>
![highlght-config](https://i.gyazo.com/c3b330403100e55b1938882f49d353fc.png)

### Advanced scouter hiding:
Options to hide highlighted with a threshold, missing layouts, and ropeless are also available. As long as the amount of highlighted rooms detected is equal or greater than the threshold, the raid will be shown. The missing layouts option hides a raid if the layout does not match with a layout in the layout whitelist(found in the default plugin settings).<br/>
![advanced-config](https://i.gyazo.com/eb72bf7da7f68901afe3e47fdc2cb489.png)

## License
CoX Scouter External Plugin is licensed under the BSD 2-Clause License. See [LICENSE](https://github.com/Blackberry0Pie/CoX-Scouter-External/blob/master/LICENSE) for details.

## Author
[Blackberry0Pie](https://github.com/Blackberry0Pie) / Truth Forger - CoX Scouter External Features<br/>
[Kamiel](https://github.com/Kamielvf) - Original Chambers of Xeric [Runelite](https://github.com/runelite/runelite) Plugin<br/>
[WooxSolo](https://github.com/WooxSolo) - Original implementation of the Chambers of Xeric [Layout Solver](https://github.com/WooxSolo/raids-layout)
