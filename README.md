# pack-rle
Implementation of RLE (run-length encoding) compression in kotlin and java.
## Usage
 Command Line: `pack-rle [-z|-u] [-out outputname.txt] inputname.txt`
- `-z` - pack a file inputname.txt
- `-u`- unpack a file inputname.txt
- `-out outputname.txt` - set output file name (Default: `inputname.txt`)

If the arguments are incorrect, a corresponding error message is generated
## Automated Tests
- `src/test/java/PackRLETest.kt`