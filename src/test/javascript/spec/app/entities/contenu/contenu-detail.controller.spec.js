'use strict';

describe('Controller Tests', function() {

    describe('Contenu Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockContenu, MockUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockContenu = jasmine.createSpy('MockContenu');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Contenu': MockContenu,
                'Utilisateur': MockUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("ContenuDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tradinglearningApp:contenuUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
