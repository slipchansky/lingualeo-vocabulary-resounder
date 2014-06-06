

document.addEventListener('DOMContentLoaded', function () {

      
      var addLeo = function (n) {
      var li = document.createElement('li')
      var anchor = document.createElement('a');
      anchor.innerHTML = n+" days";
      anchor.setAttribute('href',"#");
      
      anchor.onclick  = function() {
        chrome.tabs.getSelected(null, function(tab){
           var tabId = tab.id;
           chrome.tabs.executeScript(tabId, {code: "cookies=escape(document.cookie);cookies"}, function(response) {
              var cookies = response[0];
              chrome.tabs.create({url: "http://facematic.org/lld/go?days="+n+"&cookies="+cookies});
           });
         });
      };

      li.appendChild (anchor);
      
      document.getElementById('bookmarks').appendChild(li);
   }
   
   var redirect = function (tabId) {

     //setTimeout (function() {
     chrome.tabs.executeScript(tabId, {code: "document.location='http://lingualeo.com'"},  function() { window.close(); });
     //}, 100);
     

     //
   }
   


   chrome.tabs.getSelected(null, function(tab){
           var tabId = tab.id;
           chrome.tabs.executeScript(tabId, {code: "loc=document.location.host;loc"}, function(response) {
              if (response=="lingualeo.com") {
                  addLeo (7);
                  addLeo (14);
                  addLeo (30);
              } else {
                redirect (tabId);
                //window.close ();
              }

           });
         });



      //$('#bookmarks').append(anchor);
});
