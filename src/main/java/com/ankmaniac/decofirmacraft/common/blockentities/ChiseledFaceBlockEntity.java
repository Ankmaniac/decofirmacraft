package com.ankmaniac.decofirmacraft.common.blockentities;

//for later notes:
//use 16x16 boolean array to store which pixels have been chiseled away
//depending on which have been chiseled draw shaded pixels, highlighted pixels, or show face under
//for each pixel in the array, check its surroundings.
//if a filled corner top right/bottom left, do not change.
//if surrounded by other chiseled, do not change
//if in a line, unfilled corner, or unfilled T, draw dark
//if lining the left/top side of chiseled pixels, draw dark
//if lining right/bottom side of chiseled pixels, draw light
public class ChiseledFaceBlockEntity {
}
