# Infinity
A reliable but simple to use recyclerview infinite pagination library. Add infinite pagination with as few as three lines of code

## Motivation

In almost all my projects involving making API requests or accessing firebase data, I always need pagination. I tend to apply load more or endless pagination. There are alot of libraries out there but they just have too much fat. I needed a simple and reliable library to inform me of when I have reached the bottom of my recyclerview so that I can fetch the next page. It needed to do this without forcing me to specify the number of data I need or what page I am in, just tell me if I reach the bottom so that I can maintain the page index myself. Well that's what this library is for.

## Features
1. No bloating : Super lightweight and thin - Only three lines of code
2. Reliable because it doesn't try to do alot of unnecessary things.
3. Easy to use. Just call one method and pass your recyclerview.
4. No dependencies.

## Built with
1. AndroidX
2. Kotlin

## Installation
The library is hosted in jitpack. So to install it, first go to your root-level build.gradle and specify jitpack as a repository:

```groovy
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```
