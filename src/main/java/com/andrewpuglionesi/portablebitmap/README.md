# Portable bitmap creation
## Context
The portable bitmap (`.pbm`) file format is a simple image format that represents each pixel of an image as a single bit. When a bit is 0, the corresponding pixel is white. When a bit is 1, the corresponding pixel is black. 

(Technically, there's also a less space-efficient variant that represents each pixel as an ASCII `0` or `1`. We'll ignore that for the purpose of this kata.)

A `.pbm` file contains three parts:
1. The first line will be a format specifier, which for our use case is always the string `P4`.
2. The next line will contain two base-10 numbers separated by a space.
    - The first number is the width of the image in pixels.
    - The second number is the height of the image in pixels.
3. Finally, the third line contains the contents of the image as raw binary. The first row of pixels is written from left to write, then the second row, and so on. In the binary representation, there is no whitespace or separator between rows. **If the image width is not a multiple of 8, each row of the image is padded to a multiple of 8 bits.**

## Problem statement
Write a tool that allows programmers to create `.pbm` images. Users should be able to:

- Specify the width and height of the image in pixels
- Set individual pixels as black or white
- (Optional) Draw lines and/or shapes
- Save the image as a file

You can check your work by opening up the generated `.pbm` files on your computer. If you don't already have software that can open them, I recommend installing GIMP.

## Example .pbm file
```
P4
8 8
~~~~~~~~
```
If you save this as a `.pbm` file, open it with image-viewing software, and zoom in, you should see a small, 8x8 pixel image.

Remember: the `P4` in the first line is a format specifier that will be in every file. The `8 8` in the second line means the image is 8 pixels wide and 8 pixels high.

The 64 pixels of the image are contained in the 8 `~` characters. Each 1-byte `~` represents a single 8-pixel row.

To build a mental picture of the image, note that the `~` character has ASCII character code 126. In binary, 126 = 01111110. So, imagine stacking up 8 of these binary rows:

```
01111110
01111110
01111110
01111110
01111110
01111110
01111110
01111110
```

Since each `0` is a white pixel and each `1` is a black pixel, this image is a black rectangle with white borders on its left and right side.

Note: If you create the file with a text editor on Windows, the image may be slightly corrupted by the extra whitespace character in your newlines (`\r\n`), which will be interpreted as part of the image.

Keep in mind that the width and height won't always be a multiple of 8. In that case, each row should be padded to a multiple of 8 bits. For example, to represent a 7x7 pixel black square, we use:

```
11111110
11111110
11111110
11111110
11111110
11111110
11111110
```

Note the padding bit `0` at the end of each row. It may seem wasteful, but it's absolutely necessary if the image is to be rendered correctly.

11111110 = 254, which is the character code for þ. Thus as a PBM file, this will look something like:

```
P4
7 7
þþþþþþþ
```

Don't even bother entering this into a text editor, as you'll probably have issues with character encoding.