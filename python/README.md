Welcome to the snek zone 🐍

# Prerequisites
Install Python 3 (required) and Pip.

# Run unit tests without coverage
```bash
python3 -m unittest
```

# Installing dependencies for advanced build features
It is recommended to use [virtualenv](https://virtualenv.pypa.io/en/latest/) or a similar tool to isolate installed dependencies to this project. For simplicity's sake, you can use the virtual environment tool packaged with Python as below.
```bash
cd src/python
# create a virtual environment called venv
python3 -m venv venv
# activate the virtual environment
source venv/bin/activate # In Windows, run: venv\Scripts\activate
# install dependencies within the virtual environment
pip install -r requirements.txt
```

# Run a full build
The full build will activate your virtual environment, install dependencies, run unit tests with coverage, and run a linter. It assumes you have created a virtual environment as above.
## Linux
```bash
bash full_build
```
## Windows
```bat
.\full_build.bat
```

# Run unit tests with coverage
```bash
# collect coverage while running tests
coverage run --branch -m unittest
# report the collected coverage stats
coverage report
```

# Run linter
## Linux
```bash
# Scan all Python files under the current directory:
python3 -m pylint *
# Scan Python files in a specific module:
python3 -m pylint leetcode
```
## Windows
```bash
# Scan all Python files under the current directory:
pylint *
# Scan Python files in a specific module:
pylint leetcode
```
