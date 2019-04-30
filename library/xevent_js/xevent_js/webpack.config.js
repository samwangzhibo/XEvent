var path = require('path');
var webpack = require('webpack');
module.exports = {
   entry: './src/index.js',
   output: {
       path: path.resolve(__dirname, 'build'),
       filename: 'libxevent.js'
   },
   module: {
   },
   stats: {
       colors: true
   },
   devtool:'inline-source-map'
};
