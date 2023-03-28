Your Friendly Neighborhood Digital Assistant
Project 2-2: Group 14
Team: Alex, Dominic, Jasmijn, Noah, Ruben, Simon, Semih

Instructions:

    To start our software, please run the Run.java file located in "src\main\java\DigitalAssistant\Run.java"

    Chatting with our assistant:
        Our assistant is equiped with a few features which we are sure you will enjoy;
            - A google search feature:
                o Whenever you would like to search something up on google, simply type your query in the chat box, and then click "Google"
            - Skills:
                o Our assistent is built with a robust, customizable skill system. The asisstent has "skills" which allow the user to perform different actions.
                    The user may see what skills our digital assistent comes preinstalled with by reading the skills.txt. 
                    Each skill has the format:
                        "
                            -----
                            Name (skill name)
                            Question (Query required from the user, plus the input slots denoted by "<>")
                            Slot <SLOT> (slot1) (slot2)...etc
                            --
                            Action <SLOT> input |ACTION| (output string)
                            +++++
                        "
                    In the above example you see a skill defined wherein the name, and prototype sentence (sentence which triggers the skill), slots, and actions are defined:
                        Slots - possible inputs for corresponding slots in the prototype sentence.
                        Action - given the combination of "<SLOT> + corresponding valid input", we execute the "|ACTION|" with the output string as input for the corresponding
                        action defined in the backend of our software.
                    The current actions our program supports are |Print|, and |Search|:
                        |Print| - Takes the output string and prints it back to the user.
                        |Search| - Takes the output string and searches google with it.
                    o To use a skill, simply type the prototype sentence into the chatbox with valid inputs and be amazed!
            - Skill Editor:
                o The Skill Editor allows for the user to create their own skills! The skill editor is reached by pressing the "Skill" button in the main screen.
                    After entering this screen follow these steps:
                        1. Give your skill a name and create a prototype sentence (inside this sentence, be sure to include at least one valid slot (must be a word between "<>" such as <CITY>))
                        2. Press insert skill, then continue to define what inputs are valid for each slot by selecting the slot via the dropdown menu and adding inputs by typing into the text
                        box and pressing "add".
                        3. After you are satisfied with your inputs, press "Next".
                        4. In the next menu you will now be prompted to make the outputs associated with the permutations of your slot inputs. To make a new rule (combination of permutation + output),
                        please select a combination of values, choose an action to be performed from the dropdown box, type your output string, and then finally "Confirm and add rule" to finish your rule.
                        5. When you are done, you may press submit and voila! Your rule is now added FOREVER.
                o In the case you would like to edit a pre-existing rule you must create a new rule with the same name to overwrite the old one (be sure to rewrite all inputs).
    
    To exit our software simply close the program!

    
    Thank you for your time and we wish you happy chatting!