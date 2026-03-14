module.exports = {
  default: {
    requireModule: ['ts-node/register'],
    require: ['src/steps/**/*.ts'],
    format: [
      'progress-bar',
      'html:cucumber-report.html',
      'json:cucumber-report.json',
      'junit:cucumber-report.xml'
    ],
    formatOptions: { snippetInterface: 'async-await' },
    paths: ['features/**/*.feature'],
    timeout: 30000  // 30 seconds timeout for steps (default is 5000ms)
  }
};
