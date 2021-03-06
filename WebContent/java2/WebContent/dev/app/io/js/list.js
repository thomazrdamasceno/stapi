"use strict";
(function(){
	angular.module("stapiApp") 
	.config(function($routeProvider, $httpProvider){

	//Rota para listagem dos objetos
	$routeProvider.when("/io",{

		templateUrl:"app/io/html/list.html",

		controller: function(ioUtil, stService, $route, stUtil, $scope){
			
			 //Editar item ou cadastrar novo
			$scope.openItem = function(item){
				
				ioUtil.openItem(item, function(event, modal){
					
					/*
					 Possíveis valores para event
					 
					 * add - O item foi salvo
					 * add-error - erro ao salvar o item
					 * delete - O item foi deletado
					 * delete-error -  Erro ao deletar o objeto
					
					 */
					
					 modal.close();
					 $route.reload();
					
					
				});

			}

			//Deletar item
			$scope.deletarItem = function(item){
						
				stService.executePost("io/delete/", [item.id]).then(function(){
					
					stUtil.showMessage("","Item deletado com sucesso","info");	
					$route.reload();
					
				}).catch(function(){
					
					stUtil.showMessage("","Ocorreu um erro ao deletar","danger");	
				});

			}

		}

	}); 

})

})();
