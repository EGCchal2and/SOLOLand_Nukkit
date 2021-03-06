package com.solo.sololand.command.land;

import cn.nukkit.Server;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import com.solo.sololand.command.MainCommand;
import com.solo.sololand.command.SubCommand;
import com.solo.sololand.world.World;
import com.solo.sololand.land.Land;
import com.solo.sololand.util.Message;

import java.util.ArrayList;

public class LandGive extends SubCommand{

  public LandGive(MainCommand mCmd){
    super(mCmd, "양도", "다른 유저에게 땅을 양도합니다.");
    this.setAliases(new String[]{"주기"});
    this.setPermission("sololand.command.land.give");
  }

  @Override
  public boolean execute(CommandSender sender, String[] args){
    Player player = (Player) sender;
    World world = World.get(player.getLevel());
    Land land = world.getLand(player.getFloorX(), player.getFloorZ());
    if(land == null){
      Message.fail(player, "현재 위치에서 땅을 찾을 수 없습니다.");
      return true;
    }
    String name = player.getName().toLowerCase();
    if(!player.isOp() && !land.isOwner(name)){
      Message.fail(player, "땅 주인이 아니므로 땅을 다른 유저에게 양도할 수 없습니다.");
      return true;
    }
    if(args.length < 1){
      return false;
    }
    Player target = Server.getInstance().getPlayer(args[0]);
    if(target == null){
      Message.fail(player, args[0] + "님은 현재 온라인이 아닙니다.");
      return true;
    }
    String targetName = target.getName().toLowerCase();
    if(land.isOwner(targetName)){
      Message.fail(player, "땅 주인에게 땅을 줄 수 없습니다.");
      return true;
    }
    if(world.getMaxLandCount() < world.getLands(targetName).size()){
      Message.fail(player, target.getName() + "님이 해당 월드에서 소유할 수 있는 땅의 최대 갯수를 초과하였습니다. (최대 " + Integer.toString(world.getMaxLandCount()) + "개)");
      return true;
    }
    ArrayList<String> members = new ArrayList<String>();
    members.add(targetName);
    land.setMembers(members);
    land.setOwner(targetName);
    land.setWelcomeMessage("");
    Message.success(player, target.getName() + "님에게 " + Integer.toString(land.getNumber()) + "번 땅을 양도 처리 하였습니다.");
    Message.success(target, player.getName() + "님이 " + world.getCustomName() + " 월드의 " + Integer.toString(land.getNumber()) + "번 땅을 양도하셨습니다.");
    return true;
  }
}