from flask import *
from db import *

public=Blueprint('public',__name__)
@public.route('/',methods=['post','get'])
def home():

	return render_template("home.html")
	
@public.route('/log',methods=['post','get'])
def log():
	if 'submit' in request.form:
		uname=request.form['uname']
		pwd=request.form['pwd']
		q="select * from login where username='%s' and password='%s'"%(uname,pwd)
		res=select(q)
		if res:
			session['logid']=res[0]['logid']
			if res[0]['type']=="admin":

				
				return redirect(url_for("mainadmin.mainadminhome"))


			elif res[0]['type']=="agent":
				q="select * from healthagent  where logid='%s'"%(session['logid'])
				res=select(q)
				session['healthagent_id']=res[0]['healthagent_id']
				return redirect(url_for("admin.adminhome"))
			else:
				flash("login failed")
		else:
			flash("login failed")
	return render_template("login.html")
@public.route('/reg',methods=['post','get'])
def reg():
	if 'submit' in request.form:
		a=request.form['fname']
		b=request.form['lname']
		c=request.form['Group']
		d=request.form['dob']
		e=request.form['Gender']
		f=request.form['ph']
		g="insert into donors values(null,'%s','%s','%s','%s','%s','%s')"%(a,b,c,d,e,f)
		insert(g)
	return render_template("publicregistration.html")

@public.route('/public_healthagentreg',methods=['post','get'])
def public_healthagentreg():
	if 'submit' in request.form:
		a=request.form['fname']
		b=request.form['lname']
		d=request.form['dob']
		e=request.form['Gender']
		f=request.form['ph']
		j=request.form['qua']
		username=request.form['username']
		password=request.form['password']
		g="insert into login values(null,'%s','%s','pending')"%(username,password)
		id=insert(g)
		g="insert into healthagent values(null,'%s','%s','%s','%s','%s','%s','%s')"%(id,a,b,d,e,f,j)
		insert(g)
	return render_template("public_healthagentreg.html")