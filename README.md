# Shipbattle

## What is it / What does it do?

Shipbattle is a variant of the classic game "Battleship", where you can select individual
ships to fire rounds. Depending on whether anything was hit and what you hit, you may be allowed to fire more
shots!

## Who will use it? ##

Anyone who has (too much) free time and wants to play a modified unique twist on the classic game of Battleship against 
a bot and/or another player.

## Why is this project of interest to you? ##

I have always been very interested about making games and the video game industry as a whole.
I want to make a game (relatively) small in scope, but has a good amount of room for expansion.
Battleship is also just a very fun game!

## User Stories ##

As a user, I want to be able to:

- Choose the number of ships that are in the game, both on our team and the opponents
- Place my ships anywhere I want on the board, given there is space there
- Determine the size of the playing field
- View where the number ships I have and where they are currently located
- Select one of my ships to fire from, and fire on enemy positions using it
- Save the current state of the game (where ships are positioned, the damage inflicted on them, etc)
- Load a past save into the current game

## Phase 4: Task 2 ##
```
Wed Nov 29 15:42:13 PST 2023: Added ship A at (5,9)
Wed Nov 29 15:42:13 PST 2023: Added ship B at (4,5)
Wed Nov 29 15:42:19 PST 2023: Added ship A at (2,1)
Wed Nov 29 15:42:26 PST 2023: Added ship B at (6,4)
Wed Nov 29 15:42:35 PST 2023: Fired on board at position (6,5)
Wed Nov 29 15:42:36 PST 2023: Fired on board at position (5,5)
Wed Nov 29 15:42:37 PST 2023: Fired on board at position (4,5)
```

## Phase 4: Task 3 ##
Seeing this UML diagram, the most important thing I would do is more closely associate the board with a player, through
a bidirectional relationship.
A standalone board doesn't make any sense, and should likely contain a player that controls it.
This would decouple the GUI version of the game from the model more, and have more of a single point of access
through the GUIPlayer, which would have a reference to the board. The GUIBoard would then only have references to other
GUI elements and two players, instead of references to a bunch of classes in the model package.

