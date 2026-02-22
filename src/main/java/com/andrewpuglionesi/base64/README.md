# Base64 encoder/decoder

## Context
Base64 encodes arbitrary binary data using a small alphabet of just 64 characters. Base64's simple character set makes it useful for transmitting files and other arbitrary data between systems. Some applications of Base64 include sending email attachments, embedding images into web pages, building JSON web tokens, and of course, the famous Basic Auth header.

During encoding, Base64 breaks its input data into 6-bit chunks, maps each chunk to a single character, and concatenates these characters together. When the number of bits in the input isn't divisible by 6, most implementations add padding character(s) to the end of the Base64 string to indicate that the octets (bytes) of the input weren't evenly broken into sextets.

Refer to these resources for more details about Base64 implementation:  
[Wikipedia article on Base64](https://en.wikipedia.org/wiki/Base64)  
[IETF specification](https://datatracker.ietf.org/doc/html/rfc4648#section-4)

## Problem Statement
Without using an existing library or executable designed for the task, write a utility that can:
1. Encode data as Base64-formatted string
2. Decode a Base64 string back into the original data

### Assumptions
- Base64-encoded output must include padding
- Base64-encoded input for decoding will include padding
- Use the standard variant of Base64 (not Base64URL or another variant)
- Base64 output will be a single line of text with no added line breaks
- When decoding a Base64 string, you should completely ignore characters that are not part of the Base64 alphabet (e.g., `\n`, `*`, `&`)
  - To understand why, see [RFC 4648, Section 3.3](https://datatracker.ietf.org/doc/html/rfc4648#section-3.3)

## Examples

Original (byte array): `[0x62, 0x61, 0x6e]`  
Base64: `YmFu`  
**Explanation:**  
Imagine the input as one long string of bits: `01100010 01100001 01101110`  
Note that there are 24 bits, which evenly divides by 6. Now imagine rearranging the input into 6-bit chunks: `011000 100110 000101 101110`  
Next we can translate each chunk into a decimal number (`24`, `38`, `5`, `46`) and translate these numbers into the corresponding Base64 characters (`Y`, `m`, `F`, `u`).
Since this byte array could be evenly broken into 6-bit chunks, no padding is added to the output.

Original (byte array): `[0x62]`  
Base64: `Yg==`  
**Explanation:**  
In binary, the hexadecimal number `0x62` breaks down to `01100010`. Base64 takes the first 6 bits (`011000`, or `24` as a decimal number) and translates it to the letter `Y`.  
Two bits remain from the original byte: `10`. We extend the 2-bit chunk into a 6-bit chunk by adding 0's to the end: `100000`. This is the decimal number `32`, which translates to a lowercase `g` in the Base64 alphabet.  
Padding is added to indicate that the byte array couldn't be evenly broken into 6-bit chunks.
<hr>
Using binary input will help you better understand the inner workings of the algorithm. But to test against other Base64 encoder tools, it's easier to use strings. The following examples assume UTF-8 encoding.

Original: `b` (repeat of `[0x62]` example above)  
Base64: `Yg==`

Original: `ba`  
Base64: `YmE=`

Original: `ban` (repeat of `[0x62, 0x61, 0x6e]` example above)  
Base64: `YmFu`

Original: `bana`  
Base64: `YmFuYQ==`

Original: `banan`  
Base64: `YmFuYW4=`

Original: `banana`  
Base64: `YmFuYW5h`
