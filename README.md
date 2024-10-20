# Labyrinth Game

## Description

Labyrinth is an engaging game where the player's objective is to escape from a labyrinth. Starting at the bottom left corner, players must reach the top right corner while avoiding the evil dragon that moves randomly throughout the maze. The player can only move in four directions (left, right, up, or down) and has limited visibility, only being able to see neighboring fields within a distance of 3 units. The game records the number of labyrinths solved, and upon losing, it saves the player's name and score into a database. 

The game features a high score table displaying the top 10 scores and an option to restart the game.

## Objective

- Reach the top right corner of the labyrinth as quickly as possible.
- Avoid the dragon that moves randomly and can cause the player to lose.
- Solve all 10 levels without getting caught to win the game.

## Game Mechanics

- **Player Movement**: Players can move using the WASD keys. The movement is handled by detecting key presses and checking for collisions to avoid walls.
  
- **Dragon Movement**: The dragon starts at a random position and moves randomly within the labyrinth. If it reaches a neighboring field of the player, the player loses.

- **Limited Visibility**: The player can only see neighboring fields up to a distance of 3 units, creating a sense of challenge and suspense.

- **High Score Tracking**: The game tracks the number of labyrinths completed and stores high scores in a database using SQL.
  

## How to Play

1. Start at the bottom left corner of the labyrinth.
2. Use the WASD keys to navigate towards the top right corner.
3. Avoid the dragon while solving the labyrinth.
4. Track your progress and aim to complete all 10 levels without getting caught.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Labyrinth.git
