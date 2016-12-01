(function() {
    'use strict';
    angular
        .module('tradinglearningApp')
        .factory('Prestation', Prestation);

    Prestation.$inject = ['$resource', 'DateUtils'];

    function Prestation ($resource, DateUtils) {
        var resourceUrl =  'api/prestations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebutLivraison = DateUtils.convertDateTimeFromServer(data.dateDebutLivraison);
                        data.dateFinLivraison = DateUtils.convertDateTimeFromServer(data.dateFinLivraison);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
