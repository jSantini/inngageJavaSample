package com.example.inngageintegrationjavasample.libs;

public abstract class GrantPermission {
    protected abstract void call(int requestCode, String permissions[], int[] grantResults);
}