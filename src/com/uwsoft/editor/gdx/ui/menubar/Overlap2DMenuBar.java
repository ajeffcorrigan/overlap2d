/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.gdx.ui.menubar;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.uwsoft.editor.gdx.ui.menubar.commands.EditMenuCommand;
import com.uwsoft.editor.gdx.ui.menubar.commands.FileMenuCommand;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.util.ArrayList;


public class Overlap2DMenuBar extends MenuBar {
    private static final String TAG = Overlap2DMenuBar.class.getCanonicalName();
    private final Overlap2DMenuBarMediator mediator;
    private final FileMenu fileMenu;


    public Overlap2DMenuBar(Overlap2DMenuBarMediator mediator) {
        this.mediator = mediator;
        fileMenu = new FileMenu();
        addMenu(fileMenu);
        addMenu(new EditMenu());
        mediator.setTarget(this);
    }


    public void addScenes(ArrayList<SceneVO> scenes) {
        fileMenu.addScenes(scenes);
    }

    public void reInitScenes(ArrayList<SceneVO> scenes) {
        fileMenu.reInitScenes(scenes);
    }

    class EditMenu extends Menu {


        public EditMenu() {
            super("Edit");
            pad(5);
            addItem(new MenuItem("Cut", new EditMenuListener(EditMenuCommand.CUT)));
            addItem(new MenuItem("Copy", new EditMenuListener(EditMenuCommand.COPY)));
            addItem(new MenuItem("Paste", new EditMenuListener(EditMenuCommand.PAST)));
            addItem(new MenuItem("Undo", new EditMenuListener(EditMenuCommand.UNDO)));
            addItem(new MenuItem("Redo", new EditMenuListener(EditMenuCommand.REDO)));
        }

        private class EditMenuListener extends ChangeListener {

            private final EditMenuCommand menuCommand;

            public EditMenuListener(EditMenuCommand menuCommand) {
                this.menuCommand = menuCommand;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mediator.editMenuItemClicked(menuCommand);
            }
        }
    }

    class FileMenu extends Menu {

        private final PopupMenu scenesPopupMenu;
        private final Array<MenuItem> sceneMenuItems;

        public FileMenu() {
            super("File");
            pad(5);
            addItem(new MenuItem("New Project", new FileMenuListener(FileMenuCommand.NEW_PROJECT)));
            addItem(new MenuItem("Open Project", new FileMenuListener(FileMenuCommand.OPEN_PROJECT)));
            addItem(new MenuItem("Save Project", new FileMenuListener(FileMenuCommand.SAVE_PROJECT)));
            //
            MenuItem scenesMenuItem = new MenuItem("Scenes");
            scenesPopupMenu = new PopupMenu();
            scenesPopupMenu.addItem(new MenuItem("Create New Scene", new FileMenuListener(FileMenuCommand.CRATE_NEW_SCENE)));
            scenesPopupMenu.addItem(new MenuItem("Delete Current Scene", new FileMenuListener(FileMenuCommand.DELETE_CURRENT_SCENE)));
            scenesPopupMenu.addSeparator();
            scenesMenuItem.setSubMenu(scenesPopupMenu);
            addItem(scenesMenuItem);
            //
            addSeparator();
            addItem(new MenuItem("Import to Library", new FileMenuListener(FileMenuCommand.IMPORT_TO_LIBRARY)));
            addItem(new MenuItem("Export", new FileMenuListener(FileMenuCommand.EXPORT)));
            addItem(new MenuItem("Export Settings", new FileMenuListener(FileMenuCommand.EXPORT_SETTINGS)));
            addItem(new MenuItem("Exit", new FileMenuListener(FileMenuCommand.EXIT)));
            sceneMenuItems = new Array<>();
        }

        public void addScenes(ArrayList<SceneVO> scenes) {
            for (SceneVO sceneVO : scenes) {
                MenuItem menuItem = new MenuItem(sceneVO.sceneName, new SceneMenuItemListener(sceneVO.sceneName));
                sceneMenuItems.add(menuItem);
                scenesPopupMenu.addItem(menuItem);
            }
        }

        public void reInitScenes(ArrayList<SceneVO> scenes) {
            for (MenuItem menuItem : sceneMenuItems) {
                menuItem.remove();
            }
            sceneMenuItems.clear();
            addScenes(scenes);
        }

        private class SceneMenuItemListener extends ChangeListener {
            private final String sceneName;

            public SceneMenuItemListener(String sceneName) {
                this.sceneName = sceneName;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "sceneName : " + sceneName);
                mediator.sceneMenuItemClicked(sceneName);
            }
        }

        private class FileMenuListener extends ChangeListener {
            private final FileMenuCommand menuCommand;

            public FileMenuListener(FileMenuCommand menuCommand) {
                this.menuCommand = menuCommand;
            }

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "menuCommand : " + menuCommand);
                mediator.fileMenuItemClicked(menuCommand);
            }
        }
    }


}
