# Hamming (7,4) Error-Correction Utility (`Hamming`)

A robust Java backend utility that implements matrix-based Hamming (7,4) linear block coding, syndrome-based error detection, and automated single-bit error correction for file streams.

## Key Features

* **Matrix-Based Encoding:** Converts 4-bit data words into 7-bit codewords using generator matrix multiplication and modulo-2 arithmetic.
* **Error Detection & Correction:** Utilizes parity checks and syndrome vector decoding to pinpoint corrupted bit indexes and automatically flip them back.
* **File Stream Processing:** Reads raw binary tokens from input files (`tbe.txt`), processes encoding/decoding pipelines, and writes output results safely.
* **Automated Test Harness:** Built-in test suite (`runAllTests`) validating encoder, decoder, and parity check logic against benchmarked data.

## Project Structure
* **Language:** Java
* **Core I/O:** `FileWriter`, `PrintWriter`, `Scanner`, `File`
* **Core Classes:**
  * `Hamming.java`: Manages matrix math definitions, row encoding/decoding, parity syndrome evaluation, and test suites.
