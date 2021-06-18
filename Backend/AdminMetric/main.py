from controller import Controller
from flask import Flask
from admin_module import AdminModule

app = Flask(__name__)

if __name__ == '__main__':
	s = "mysql+pymysql://tntrol:password@localhost:3306/mytp2"
	#s = "mysql+pymysql://i7lukqqtwtt3csbt:nn40pafo0k3ephki@u3r5w4ayhxzdrw87.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/jals87gr7rk8l2sk"
	list_m = [ AdminModule()]
	c = Controller(app, s, list_m)
	c.run()