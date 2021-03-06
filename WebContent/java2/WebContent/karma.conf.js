//Configurações do Karma para utilização na Integração contínua

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
	            
     'dev/global/lib/jquery.min.js',
     'dev/global/lib/angular-1.6.9/angular.js',
     'dev/global/lib/angular-1.6.9/**/*.js',
     'dev/global/lib/angular-locale_pt-br.js',
     'dev/global/lib/angular-material/angular-material.min.js',
     'dev/global/lib/jquery-ui.js',
     'dev/global/lib/angular/angular-spinner.js',
     'dev/global/lib/Chart.js',
     'dev/global/lib/angular-chart.js',
     'dev/global/lib/**/*.js',
     'dev/js/base.js',
     'dev/app-config.js',
     'dev/global/st-api/*/*.js',
     'dev/global/st-api/**/*.js',
     'dev/app/**/*.js',
     "dev/**/*.html",
     "dev/*.svg"
   
    ],



    // list of files to exclude
    exclude: [
    	  'bower_components/stapiweb/**/*.js',
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
    	'dev/**/*.html': ['ng-html2js']
    	
    	
     },
     
     
     ngHtml2JsPreprocessor: {
    	 stripPrefix: 'dev/',
         moduleName: 'stapi.templates',
      
       },
     
    plugins: [
              'karma-jasmine',
               'karma-chrome-launcher',
              'karma-spec-reporter',
              'karma-ng-html2js-preprocessor',
              'karma-browserifast'
          ],


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['spec'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,

    //
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // start these browsers
    // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
    browsers: ['Chrome'],

    captureTimeout: 60000,
    

    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false
  })
}
