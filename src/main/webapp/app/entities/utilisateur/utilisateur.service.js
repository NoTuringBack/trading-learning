(function() {
    'use strict';
    angular
        .module('tradinglearningApp')
        .factory('Utilisateur', Utilisateur);

    Utilisateur.$inject = ['$resource'];

    function Utilisateur ($resource) {
        var resourceUrl =  'api/utilisateurs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
