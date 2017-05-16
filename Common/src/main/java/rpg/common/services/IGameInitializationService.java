/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg.common.services;

import rpg.common.data.GameData;
import rpg.common.world.World;

public interface IGameInitializationService {
    void initialize(World world, GameData gameData);
}
