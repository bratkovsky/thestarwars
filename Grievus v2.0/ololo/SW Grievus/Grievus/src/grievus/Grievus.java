package grievus;
 
import jgame.*;
import jgame.platform.*;

public class Grievus extends JGEngine {
	public static void main(String [] args) {
            new Grievus(0,0); 
        }
	public Grievus() {
            initEngineApplet(); 
        }
	public Grievus(int x,int y){
            initEngine(x,y);
        }
	public void initCanvas() {
            setCanvasSettings(20,15,32,32,null,null,null); 
        }
	public void initGame() {
		defineMedia("Grievus.tbl");
		setBGImage("bgimg");
		setFrameRate(40,4);
		setGameState("Title");
		
	}
	int timer=0, score=0, lives=4;
	double gamespeed=1.0;
	public void doFrameTitle() {
             if (getKey(KeyEsc)) System.exit(0);
		if (getKey(' ')) {
			new Grievus.Player(pfWidth()/2-25,pfHeight()-113);
			setGameState("InGame");
			score=0;
                        lives=4;
			gamespeed=1.0;
		}
	}
	public void doFrameInGame() {
		moveObjects();
		checkCollision(2,1); // корабль к игроку
		checkCollision(4,2); // пули  к кораблю
               
                timer++;
		if (gamespeed<2) gamespeed += 0.0001;
		if (timer%(int)(20/gamespeed) == 0) {
			new Grievus.enemy("chaser",random(0,pfWidth()-64), -90, (int)random(-1,1,2),2, "enemies" );
                }
                if (score>0&&(score%100)==0) {
                  if (timer%(int)(20/gamespeed) == 0)
                    new Grievus.linkor("linor", random(0,pfWidth()-64), -90, 0, 2, "linkor");
                
                }
                       
	}
                
	public void paintFrame() {
		drawImageString("SCORE "+score,20,5,-1,"font_map",32,0);
                drawImageString("LIVES: "+lives,370,5,-1,"font_map",32,0);
	}
	public void paintFrameGameOver() {
		drawImageString("GAME OVER",165,200,-1,"font_map",32,0);
	}
	public void paintFrameTitle() {
		drawImageString("THE STAR WARS",120,150,-1,"font_map",32,0);
		drawImageString("",120,200,-1,"font_map",32,0);
		drawImageString("PRESS SPACE TO BEGIN",10,300,-1,"font_map",32,0);
                drawImageString("ESC TO EXIT",120,400,-1,"font_map",32,0);
		drawImageString("USE ARROWS TO MOVE ",10,350,-1,"font_map",32,0);

	}
	public class enemy extends JGObject {
		public enemy(String n,double x, double y, int dir,int id,String t) {
			super(n,true,x,y,id,t);
			setSpeedAbs(dir*2.0*gamespeed,0);
		}
		public void move() {
				y += 2.0 * gamespeed; 
			if (xdir<0) { setAnim("sh_r"); } else { setAnim("sh_l"); }
			if (x < 0) xdir = 1;
			if (x > pfWidth()-64)  xdir=-1;
		}
		public void hit(JGObject obj) {
			new JGObject("explo",true,x,y,0,"explo", 0,0, 32);
			playAudio("explo");
			remove();
			obj.remove();
			score += 5;
		}
	}
        public class linkor extends enemy {
            int health;
        public linkor(String n,double x, double y, int dire,int id,String t){
          super(n,x,y,dire,id,t);
                    setSpeedAbs(dire*2.0*gamespeed,0);
                        health=21;
        }
        public void move() {
				y += 1.8 * gamespeed; 
			setAnim("lk"); 
		}
		public void hit(JGObject obj) {
                    if(health<1){
			new JGObject("explo",true,x,y,0,"explo", 0,0, 32);
			playAudio("explo");
			remove();
			obj.remove();
			score += 20;
                    }
                    else health--;
		}
        }

	public class Player extends JGObject {
		public Player(double x, double y) {
			super("player",false,x,y,1,"player"); 
		}
		public void move() {
                if (getKey(KeyEsc)) System.exit(0);
                setAnim("lol");
                if (getKey(KeyLeft)  && x > 14)              x -= 15*gamespeed;
                if (getKey(KeyRight) && x < pfWidth()-51-14) x += 15*gamespeed;
                if (getKey(' ')) {
                        if (countObjects("bullet",0) < 2) {
                                new JGObject("bullet",true,x+12,y-8,4,"bullet",0,-14,-2);
                                playAudio("shoot");
                                clearKey(' ');
                        }
                }
            } 
		
		public void hit(JGObject obj) {
                    if(lives>0){removeObjects(null,2);lives--;
                    }
                    else{ new JGObject("explo",true,x,y,0,"explo", 0,0, 32);
                        
			remove();
			addGameState("GameOver");
			new JGTimer(100,true) {
                            public void alarm() {
					setGameState("Title"); removeObjects(null,0); 
                            } 
                        };}
                
		}
	}
}