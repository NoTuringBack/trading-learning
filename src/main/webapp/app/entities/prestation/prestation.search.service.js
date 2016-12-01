(function() {
    'use strict';

    angular
        .module('tradinglearningApp')
        .factory('PrestationSearch', PrestationSearch);

    PrestationSearch.$inject = ['$resource'];

    function PrestationSearch($resource) {
        var resourceUrl =  'api/_search/prestations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
