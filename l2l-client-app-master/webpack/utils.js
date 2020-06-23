const fs = require('fs');
const path = require('path');

module.exports = {
    parseVersion,
    root,
    isExternalLib
};

// Returns the second occurrence of the version number from `build.gradle` file
function parseVersion() {
    // const versionRegex = /^version\s*=\s*[',"]([^',"]*)[',"]/gm; // Match and group the version number
    // const buildGradle = fs.readFileSync('build.gradle', 'utf8');
    return '0.0.1-SNAPSHOT';
}

const _root = path.resolve(__dirname, '..');

function root(args) {
  args = Array.prototype.slice.call(arguments, 0);
  const tPath = path.join.apply(path, [_root].concat(args));
  console.log("_root "+_root + " output path : "+tPath);
  console.log("ContextReplacementPlugin : "+path.resolve(__dirname, 'src') )
  return tPath;
}

function isExternalLib(module, check = /node_modules/) {
    const req = module.userRequest;
    if (typeof req !== 'string') {
        return false;
    }
    return req.search(check) >= 0;
}
