(function() {
    'use strict';
    angular
        .module('tradinglearningApp')
        .factory('Contenu', Contenu);

    Contenu.$inject = ['$resource', 'DateUtils'];

    function Contenu ($resource, DateUtils) {
        var resourceUrl =  'api/contenus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreation = DateUtils.convertLocalDateFromServer(data.dateCreation);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateCreation = DateUtils.convertLocalDateToServer(copy.dateCreation);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateCreation = DateUtils.convertLocalDateToServer(copy.dateCreation);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
