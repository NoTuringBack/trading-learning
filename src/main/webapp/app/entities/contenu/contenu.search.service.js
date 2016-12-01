(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .factory('ContenuSearch', ContenuSearch);

    ContenuSearch.$inject = ['$resource'];

    function ContenuSearch($resource) {
        var resourceUrl =  'api/_search/contenus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
