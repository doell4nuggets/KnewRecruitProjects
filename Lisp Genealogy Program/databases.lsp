(defvar *database*)
(setf *database* NIL)

(defstruct person name sex children parents)

; my family tree-part of it anyway
(push (setf person1 (make-person :name 'bud :sex 'male :children '(vince joann marcia) :parents '() )) *database* )
(push (setf person2 (make-person :name 'vince :sex 'male :children '() :parents '(bud doris) )) *database* )
(push (setf person3 (make-person :name 'doris :sex 'female :children '(vince joann marcia) :parents '() )) *database* )
(push (setf person4 (make-person :name 'joann :sex 'female :children '() :parents '(bud doris) )) *database* )
(push (setf person5 (make-person :name 'marcia :sex 'female :children '(tonya chris) :parents '(bud doris) )) *database* )
(push (setf person6 (make-person :name 'rick :sex 'male :children '(tonya chris) :parents '() )) *database* )
(push (setf person7 (make-person :name 'tonya :sex 'female :children '(jordan taylor) :parents '(rick marcia) )) *database* )
(push (setf person8 (make-person :name 'chris :sex 'male :children '(tabitha hope cal mack) :parents '(rick marcia) )) *database* )
(push (setf person9 (make-person :name 'angie :sex 'female :children '(tabitha hope cal mack) :parents '() )) *database* )
(push (setf person10 (make-person :name 'brad :sex 'male :children '(jordan taylor) :parents '(homer elaine) )) *database* )
(push (setf person11 (make-person :name 'jordan :sex 'male :children '() :parents '(brad tonya) )) *database* )
(push (setf person12 (make-person :name 'taylor :sex 'male :children '() :parents '(brad tonya) )) *database* )
(push (setf person13 (make-person :name 'cal :sex 'male :children '() :parents '(chris angie) )) *database* )
(push (setf person14 (make-person :name 'mack :sex 'male :children '() :parents '(chris angie) )) *database* )
(push (setf person15 (make-person :name 'hope :sex 'female :children '() :parents '(chris angie) )) *database* )
(push (setf person16 (make-person :name 'tabitha :sex 'female :children '() :parents '(chris angie) )) *database* )
(push (setf person17 (make-person :name 'homer :sex 'male :children '(cindy brad) :parents '() )) *database* )
(push (setf person18 (make-person :name 'elaine :sex 'female :children '(cindy brad) :parents '() )) *database* )
(push (setf person19 (make-person :name 'cindy :sex 'female :children '(margaret) :parents '(homer elaine) )) *database* )
(push (setf person20 (make-person :name 'margaret :sex 'female :children '() :parents '(cindy) )) *database* )

(princ *database*)

; random database
(push (setf person1 (make-person :name 'john :sex 'male :children '(andy grace) :parents '() )) *database* )
(push (setf person2 (make-person :name 'sara :sex 'female :children '(andy grace) :parents '() )) *database* )
(push (setf person3 (make-person :name 'dick :sex 'male :children '(willie brad) :parents '() )) *database* )
(push (setf person4 (make-person :name 'jane :sex 'female :children '(willie brad) :parents '() )) *database* )
(push (setf person5 (make-person :name 'andy :sex 'male :children '() :parents '(john sara) )) *database* )
(push (setf person6 (make-person :name 'grace :sex 'female :children '(jim) :parents '(john sara) )) *database* )
(push (setf person7 (make-person :name 'jim :sex 'male :children '(emily) :parents '(grace) )) *database* )
(push (setf person8 (make-person :name 'brad :sex 'male :children '() :parents '(dick jane) )) *database* )
(push (setf person9 (make-person :name 'willie :sex 'male :children '() :parents '(dick jane) )) *database* )
(push (setf person10 (make-person :name 'emily :sex 'female :children '() :parents '(jim) )) *database* )