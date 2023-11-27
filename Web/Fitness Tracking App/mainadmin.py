from flask import *
from db import *
import uuid 
mainadmin=Blueprint('mainadmin',__name__)
@mainadmin.route('/mainadminhome',methods=['post','get'])
def mainadminhome():
	return render_template('mainadminhome.html')

@mainadmin.route('/mainadmin_view_healthagents',methods=['post','get'])
def mainadmin_view_healthagents():
	data={}
	q="select *,concat(fname,' ',lname)as Name from healthagent inner join login using(logid)"
	res=select(q)
	data['viewagent']=res
	if "aid" in request.args:
		aid=request.args['aid']
		q="update login set type='agent' where logid='%s'" %(aid)
		update(q)
		flash("Accepted Successfully")
		return redirect(url_for('mainadmin.mainadmin_view_healthagents')) 	
	elif "rid" in request.args:
		rid=request.args['rid']
		q="update login set type='reject' where logid='%s'" %(rid)
		update(q)
		flash("Rejected Successfully")
		return redirect(url_for('mainadmin.mainadmin_view_healthagents')) 	


	return render_template('mainadmin_view_healthagents.html',data=data)

@mainadmin.route('/mainadmin_view_user',methods=['post','get'])
def mainadmin_view_user():
	data={}
	q="select *,concat(fname,' ',lname)as Name from users"
	res=select(q)
	data['viewuser']=res
	return render_template("mainadmin_view_user.html",data=data)

@mainadmin.route('/mainadmin_viewfeedback',methods=['post','get'])
def mainadmin_viewfeedback():
	data={}
	q="select *,Concat(fname,' ',lname)as Name from feedback inner join users using(userid)"
	res=select(q)
	data['viewfeed']=res
	j=0
	for i in range(1,len(res)+1):
		print('submit'+str(i))
		if 'submit'+str(i) in request.form:
			reply=request.form['reply'+str(i)]
			print(reply)
			print(j)
			print(res[j]['feed_id'])
			q="update feedback set reply='%s' where feed_id='%s'" %(reply,res[j]['feed_id'])
			print(q)
			update(q)
			return redirect(url_for('mainadmin.viewfeedback')) 	
		j=j+1
	return render_template("viewfeedback.html",data=data)


@mainadmin.route('/admin_addsamplevideo',methods=['post','get'])
def admin_addsamplevideo():
	data={}
	q="select * from sampleviceo"
	res=select(q)
	data['viewfeed']=res
	
	if 'submit' in request.form:
		img=request.files['img']
		path="static/img"+str(uuid.uuid4())+img.filename
		img.save(path)
		name=request.form['name']
		q="insert into sampleviceo values(null,'%s','%s')"%(name,path)
		insert(q)
		return redirect(url_for('mainadmin.admin_addsamplevideo')) 	
		
	return render_template("admin_addsamplevideo.html",data=data)