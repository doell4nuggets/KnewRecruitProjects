#|
Program Name: Genealogy in Lisp

Author: Taylor Doell

Class: CSC 461 - Programming Languages

Date Due: December 6, 2015
Date Programmed: 18 November 2015 - 5 December 2015

Description:
    This program takes in one argument that opens a file and reads the
    database of people into the program. If no filename is provided, it outputs
    a usage statement. If the file cannot be opened, it outputs an error message
    saying it can't open the file. The program is then ran from the CLISP REPL
    (read eval print loop). The list of queries available to use on the database
    are parents, mothers, fathers, children, sons, daughters, siblings, sisters,
    brothers, grandparents, grandfathers, grandmothers, grandchildren, grandsons,
    granddaughters, uncles, aunts, nieces, nephews, cousins, male-cousins,
    female-cousins, ancestors, male-ancestors, female-ancestors, descendants,
    male-descendants, and female-descendants. To use a querie, type
    (queriename 'nameofperson). If the person isn't found or doesn't have the
    family members, nil is returned. The purpose of this program is to help find
    the Genealogy of the family tree database file that is passed in.

Compilation Instructions: CLISP Compiler

Usage: clisp -repl genealogy.lsp DatabaseFileName
       (querie-name 'nameofperson)

Errors: No known errors
|#

(defstruct person name gender parents children)
(setf *database* nil)

#|
Author: Taylor Doell

Name: main

Description: This function passes the filename into the readDatabase method to
             open the file and read the database into the program.

Parameters:
    args: arguments passed into the program
|#
(defun main ( args )
    "(main args): emulate a main function, called with command-line args"
    (readDatabase (nth 0 args))
)

#|
Author: Taylor Doell

Name: outputUsage

Description: This function outputs the usage statement for the program.

Parameters:
    NONE
|#
(defun outputUsage()
    (print "Usage: clisp -repl genealogy.lsp DatabaseFileName")
)

#|
Author: Taylor Doell

Name: readDatbase

Description: This function opens the file with the filename specified from the
             command line arguments. Next it reads in all of the people into the
             database. After it has read in all the people, it closes the file.
             Null checks, opening the file, and the do loop to read in the file
             was taken from Dr. Weiss's fileio.lsp file.

Parameters:
    filename: The name of the file to be opened
|#
(defun readDatabase(filename)
    ; Check to see if filename was supplied
    (when (null filename) (return-from readDatabase (outputUsage)))

    ; Open the file with the filename passed in as an argument
    (setf fin (open filename :if-does-not-exist nil))

    ; If file couldn't be opened, output error message and return
    (when (null fin) (return-from readDatabase (print "Error: cannot open file")))

    (do ((data (read fin nil) (read fin nil)))      ; read entire file, returning NIL at EOF
        ((null data) 'done)                         ; exit when file is read

        ; Assign data read in to a person and add him to the database
        (push (make-person
            :name (nth 0 data)
            :gender (nth 1 data)
            :parents (nth 2 data)
            :children (nth 3 data)) *database*)
    )

    ; Close the file
    (close fin)
)

#|
Author: Taylor Doell

Name: children

Description: This function uses the name of the person passed in to query the
             database for that person and then it accesses the children of that
             person.

Parameters:
    name: name of person used for query
|#
(defun children(name)
    ; Query the database for the name given and access the children
    ; If name is not found, do nothing and return nil
    (if (null (find name *database* :key 'person-name)) ()
        (person-children (find name *database* :key 'person-name))
    )
)

#|
Author: Taylor Doell

Name: parents

Description: This function uses the name of the person passed in to query the
             database for that person. Then it accesses the parents of that
             person.

Parameters:
    name: name of person used for query
|#
(defun parents(name)
    ; Query the database for the name given and access the parents
    ; If name is not found, do nothing and return nil
    (if (null (find name *database* :key 'person-name)) ()
        (person-parents (find name *database* :key 'person-name))
    )
)

#|
Author: Taylor Doell

Name: fathers

Description: This function calls the previously defined parents function to get
             the parents of the name given. It then passes those parents into
             the malefilter to get the fathers.

Parameters:
    name: name of person used for query
|#
(defun fathers(name)
    ; Get the parents of the person and then use the malefilter on that list
    (malefilter (parents name))
)

#|
Author: Taylor Doell

Name: mothers

Description: This function calls the previously defined parents function to get
             the parents of the name given. It then passes those parents into
             the femalefilter to get the mothers.

Parameters:
    name: name of person used for query
|#
(defun mothers(name)
    ; Get the parents of the person and then use the femalefilter on that list
    (femalefilter (parents name))
)

#|
Author: Taylor Doell

Name: sons

Description: This function calls the previously defined children function to get
             the children of the name given. It then passes those children into
             the malefilter to get the sons.

Parameters:
    name: name of person used for query
|#
(defun sons(name)
    ; Get the children of the person and then use the malefilter on that list
    (malefilter (children name))
)

#|
Author: Taylor Doell

Name: daughters

Description: This function calls the previously defined children function to get
             the children of the name given. It then passes those children into
             the femalefilter to get the daughters.

Parameters:
    name: name of person used for query
|#
(defun daughters(name)
    ; Get the children of the person and then use the femalefilter on that list
    (femalefilter (children name))
)

#|
Author: Taylor Doell

Name: siblings

Description: This function calls the previously defined parents function to get
             the parents of the name given. It then loops through the parents
             to get their children and put them into a list. It then removes the
             duplicates and removes the name passed in.

Parameters:
    name: name of person used for query
|#
(defun siblings(name)
    (let (siblingsList)
        ; Loop through the parents of the name passed in getting there children
        (dolist (names (parents name))
            ; Append children to the siblingsList
            (setf siblingsList (append (children names) siblingsList))
        )

        ; Remove duplicates and the name passed in to the function
        (setf siblingsList(remove-duplicates siblingsList))
        siblingsList(remove name siblingsList)
    )
)

(defun brothers(name)
    ; Pass siblings into the malefilter
    (malefilter (siblings name))
)

(defun sisters(name)
    ; Pass siblings into the femalefilter
    (femalefilter (siblings name))
)

#|
Author: Taylor Doell

Name: grandparents

Description: This function calls the previously defined parents function to get
             the parents of the name given. It then loops through the parents
             to get their parents and appends them into a list.

Parameters:
    name: name of person used for query
|#
(defun grandparents(name)
    (let (grandparentList)
        ; Loop through the parents of name given and add to the list
        (dolist (names (parents name))
            (setf grandparentList (append (parents names) grandparentList))
        )
        (return-from grandparents grandparentList)
    )
)

#|
Author: Taylor Doell

Name: grandfathers

Description: This function calls the previously defined grandparents function to get
             the grandparents of the name given. It then passes those grandparents into
             the malefilter to get the grandfathers.

Parameters:
    name: name of person used for query
|#
(defun grandfathers(name)
    ; Pass grandparents into the malefilter
    (malefilter (grandparents name))
)

#|
Author: Taylor Doell

Name: grandmothers

Description: This function calls the previously defined grandparents function to get
             the grandparents of the name given. It then passes those grandparents into
             the femalefilter to get the grandmothers.

Parameters:
    name: name of person used for query
|#
(defun grandmothers(name)
    ; Pass grandparents into the femalefilter
    (femalefilter (grandparents name))
)

#|
Author: Taylor Doell

Name: grandchildren

Description: This function calls the previously defined children function to get
             the children of the name given. It then passes those children names
             into the children function to get the grandchildren.

Parameters:
    name: name of person used for query
|#
(defun grandchildren (name)
    (let (grandchildrenList)
        ; Loop through the children of name given and add to the list
        (dolist (names (children name))
            (setf grandchildrenList (append (children names) grandchildrenList))
        )
        (return-from grandchildren grandchildrenList)
    )
)

#|
Author: Taylor Doell

Name: grandsons

Description: This function calls the previously defined grandchildren function
             to get the grandchildren of the name given. It then passes those
             children into the malefilter to get the grandsons.

Parameters:
    name: name of person used for query
|#
(defun grandsons(name)
    ; Pass grandchildren into the malefilter
    (malefilter (grandchildren name))
)

#|
Author: Taylor Doell

Name: granddaughters

Description: This function calls the previously defined grandchildren function
             to get the grandchildren of the name given. It then passes those
             children into the femalefilter to get the granddaughters.

Parameters:
    name: name of person used for query
|#
(defun granddaughters(name)
    ; Pass grandchildren into the femalefilter
    (femalefilter (grandchildren name))
)

#|
Author: Taylor Doell

Name: uncles

Description: This function finds the uncles of the name passed in. It finds the
             parents of the name given. After acquiring the parents, it gets the
             children of the parents. It uses those children to find the parents
             of the siblings to be able to find the uncles that are uncles by
             marriage. Finally, the function removes duplicate names and then
             sends that final list into the malefilter to get the males.

Parameters:
    name: name of person used for query
|#
(defun uncles(name)
    (let (uncleList childrenList siblingsList)
        ; Loop through parents and get their siblings
        (dolist (names (parents name))
            (setf siblingsList (append (siblings names) siblingsList))
        )

        (dolist (names siblingsList)
        ; Loop through siblings to get their children
            (setf childrenList (append (children names) childrenList))
        )

        ; Loop through the childrenList to get their parents
        (dolist (names childrenList)
            (setf uncleList (append (parents names) uncleList))
        )

        ; Remove duplicates and pass them to the malefilter
        (malefilter (remove-duplicates uncleList))
    )
)

#|
Author: Taylor Doell

Name: aunts

Description: This function finds the aunts of the name passed in. It finds the
             parents of the name given. After acquiring the parents, it gets the
             children of the parents. It uses those children to find the parents
             of the siblings to be able to find the aunts that are aunts by
             marriage. Finally, the function removes duplicate names and then
             sends that final list into the femalefilter to get the females.

Parameters:
    name: name of person used for query
|#
(defun aunts(name)
    (let (auntList childrenList siblingsList)
        ; Loop through parents and get their siblings
        (dolist (names (parents name))
            (setf siblingsList (append (siblings names) siblingsList))
        )

        ; Loop through siblings and get their children
        (dolist (names siblingsList)
            (setf childrenList (append (children names) childrenList))
        )

        ; Loop through children and get their parents
        (dolist (names childrenList)
            (setf auntList (append (parents names) auntList))
        )

        ; Remove duplicates and send to femalefilter
        (femalefilter (remove-duplicates auntList))
    )
)

#|
Author: Taylor Doell

Name: nephews

Description: This function finds the uncles of the name passed in. It first finds
             the parents of the kids of the name passed in. Then it loops through
             those parents and gets the siblings of those parents. It then loops
             through them and gets their children. Next, the function removes
             the duplicate names and then passes the names through the male
             filter.

Parameters:
    name: name of person used for query
|#
(defun nephews(name)
    (let (newphewList kidParentsList sibParentsList)
        ; Get parents of your kids
        (dolist (names (children name))
            (setf kidParentsList (append (parents names) kidParentsList))
        )

        ; Remove duplicate names from the list
        (setf kidParentsList (remove-duplicates kidParentsList))

        ; Loop through the parents of your kids to get nephews
        (dolist (parent kidParentsList)
            ; Loop through the siblings of name given and add to list
            (dolist (names (siblings parent))
                (setf sibParentsList (append (children names) sibParentsList))
            )
        )

        ; Remove duplicate names and send to male filter
        (malefilter (remove-duplicates sibParentsList))
    )
)

#|
Author: Taylor Doell

Name: nieces

Description: This function finds the aunts of the name passed in. It first finds
             the parents of the kids of the name passed in. Then it loops through
             those parents and gets the siblings of those parents. It then loops
             through them and gets their children. Next, the function removes
             the duplicate names and then passes the names through the female
             filter.

Parameters:
    name: name of person used for query
|#
(defun nieces(name)
    (let (niecesList kidParentsList sibParentsList)
        ; Get parents of your kids
        (dolist (names (children name))
            (setf kidParentsList (append (parents names) kidParentsList))
        )

        ; Remove duplicate names from the list
        (setf kidParentsList (remove-duplicates kidParentsList))

        ; Loop through the parents of your kids to get nieces
        (dolist (parent kidParentsList)
            ; Loop through the siblings of name given and add to list
            (dolist (names (siblings parent))
                (setf sibParentsList (append (children names) sibParentsList))
            )
        )

        ; Remove duplicate names and send to female filter
        (femalefilter (remove-duplicates sibParentsList))
    )
)

#|
Author: Taylor Doell

Name: cousins

Description: This function uses the name passed in to find the cousins for that
             person. It acquires the children on the aunts and uncles side and
             removes the duplicates if there are any.

Parameters:
    name: name of person used for query
|#
(defun cousins(name)
    (let (cousinsList)
        ; Loop through list of uncles and get their children
        (dolist (uncle (uncles name))
            (setf cousinsList (append (children uncle)))
        )
        (dolist (aunt (aunts name))
        ; Loop through list of aunts and get their children
            (setf cousinsList (append (children aunt)))
        )

        ; Remove duplicates from the list
        (remove-duplicates cousinsList)
    )
)

#|
Author: Taylor Doell

Name: male-cousins

Description: This function calls the previously defined cousins function and
             passes it into the male filter to get the male-cousins.

Parameters:
    name: name of person used for query
|#
(defun male-cousins(name)
    ; Pass cousins into the malefilter
    (malefilter (cousins name))
)

#|
Author: Taylor Doell

Name: female-cousins

Description: This function calls the previously defined cousins function and
             passes it into the female filter to get the female-cousins.

Parameters:
    name: name of person used for query
|#
(defun female-cousins(name)
    ; Pass cousins into the femalefilter
    (femalefilter (cousins name))
)

#|
Author: Taylor Doell

Name: ancestors

Description: This function recursively calls itself to climb the tree to find
             all the ancestors of the name passed in. Once all the ancestors are
             found, it removes duplicates if any are found.

Parameters:
    name: name of person used for query
|#
(defun ancestors(name)
    (let (ancestorsList)
        ; If the parents for the name given are null return
        (if (null (parents name)) ()
            ; Loop through the names the parents
            (dolist (names (parents name))
                ; Push the name onto the ancestorsList
                (push names ancestorsList)
                ; Append the recursive call onto the ancestorsList
                (setf ancestorsList (append (ancestors names) ancestorsList))
            )
        )

        ; Remove the duplicate names
        (remove-duplicates ancestorsList)
    )
)

#|
Author: Taylor Doell

Name: male-ancestors

Description: This function calls the previously defined ancestor function and
             then passes that list into the malefilter into the get the
             male-ancestors.

Parameters:
    name: name of person used for query
|#
(defun male-ancestors(name)
    ; Pass the ancestors into the malefilter method
    (malefilter (ancestors name))
)

#|
Author: Taylor Doell

Name: female-ancestors

Description: This function calls the previously defined ancestor function and
             then passes that list into the femalefilter into the get the
             female-ancestors.

Parameters:
    name: name of person used for query
|#
(defun female-ancestors(name)
    ; Pass the ancestors into the femalefilter method
    (femalefilter (ancestors name))
)

#|
Author: Taylor Doell

Name: descendants

Description: This function gets the descendants of the name given by recursively
             traversing the family tree. It then removes all the duplicate names.

Parameters:
    name: name of person used for query
|#
(defun descendants(name)
    (let (descendantList)
        ; If the parents for the name given are null return
        (if (null (children name)) ()
            ; Loop through the names the parents
            (dolist (names (children name))
                ; Push the name onto the ancestorsList
                (push names descendantList)
                ; Append the recursive call onto the ancestorsList
                (setf descendantList (append (descendants names) descendantList))
            )
        )

        ; Remove duplicate names
        (remove-duplicates descendantList)
    )
)

#|
Author: Taylor Doell

Name: male-descendants

Description: This function calls the previously defined descendants function and
             passes that list into the malefilter.

Parameters:
    name: name of person used for query
|#
(defun male-descendants(name)
    ; Pass the descendants into the malefilter method
    (malefilter (descendants name))
)

#|
Author: Taylor Doell

Name: female-descendants

Description: This function calls the previously defined descendants function and
             passes that list into the femalefilter.

Parameters:
    name: name of person used for query
|#
(defun female-descendants(name)
    ; Pass the descendants into the femalefilter method
    (femalefilter (descendants name))
)

#|
Author: Taylor Doell

Name: malefilter

Description: This function finds each name from the list passed in the database.
             It then uses an if statement to check if that person is male. If so,
             it pushes the name onto the list to return.

Parameters:
    peopleList: list of people to filter
|#
(defun malefilter(peopleList)
    (let (maleList currentPerson)
        ; Loop through peopleList getting their person out of the database
        (dolist(name peopleList)
            ; Get person from database with name from peopleList
            (setf currentPerson (find name *database* :key 'person-name))
            ; If person is male, enter if statement
            (if (string= (person-gender currentPerson) 'male)
                ; Push male on the list
                (push name maleList)
            )
        )

        ; Return list from function (prevents getting nil if last name is female)
        (return-from malefilter maleList)
    )
)

#|
Author: Taylor Doell

Name: femalefilter

Description: This function finds each name from the list passed in the database.
             It then uses an if statement to check if that person is female. If
             so, it pushes the name onto the list to return.

Parameters:
    peopleList: list of people to filter
|#
(defun femalefilter(peopleList)
    (let (femaleList currentPerson)
        ; Loop through peopleList getting their person out of the database
        (dolist(name peopleList)
            ; Get person from database with name from peopleList
            (setf currentPerson (find name *database* :key 'person-name))
            ; If person is male, enter if statement
            (if (string= (person-gender currentPerson) 'female)
                ; Push female onto the list
                (push name femaleList)
            )
        )

        ; Return list from function (prevents getting nil if last name is male)
        (return-from femalefilter femaleList)
    )
)

; call the main function, passing command-line arguments
(main *ARGS*)
