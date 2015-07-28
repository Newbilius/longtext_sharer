package com.newbilius.longtextsharer

public class Size(val width:Int,val height:Int);

fun Int.x(height:Int):Size{
    return Size(this,height);
}