package gg.igni.igniserver.watch.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import gg.igni.igniserver.watch.service.ViewService;

@ShellComponent
public class ShellController {

  @Autowired
  private ViewService viewService;

  @ShellMethod(key = "set_two_views", value = "Populate embed of selected id with two views.")
  @Transactional
  public boolean setTwoViews(@ShellOption Long id)
  {
    return viewService.testData(id);
  }
}
