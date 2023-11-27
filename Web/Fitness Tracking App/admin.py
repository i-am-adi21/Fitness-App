from flask import *
from db import *
import uuid
admin=Blueprint('admin',__name__)
@admin.route('/adminhome',methods=['post','get'])
def adminhome():
	return render_template('adminhome.html')
@admin.route('/createdietrule',methods=['post','get'])
def createdietrule():
	data={}
	if 'submit' in request.form:
		title=request.form['Title']
		Desc=request.form['desc']
		q="insert into generaldiet values(null,'%s','%s')"%(title,Desc)
		insert(q)
	q="select * from generaldiet"
	res=select(q)
	data['viewdiet']=res
	return render_template('createdietrule.html',data=data)
@admin.route('/viewuser',methods=['post','get'])
def viewuser():
	data={}
	q="select *,concat(fname,' ',lname)as Name from users"
	res=select(q)
	data['viewuser']=res
	return render_template("viewregisteredusers.html",data=data)



@admin.route('/viewfeedback',methods=['post','get'])
def viewfeedback():
	data={}
	q="select *  from feedback inner join users using (userid)"
	res=select(q)
	data['viewuser']=res
	j=0
	for i in range(1,len(res)+1):
		if 'submit'+str(i) in request.form:
			reply=request.form['reply'+str(i)]
			q="update feedback set reply='%s' where feed_id='%s'"%(reply,res[j]['feed_id'])
			update(q)
			flash("message send")
			return redirect(url_for("admin.viewfeedback"))
	j=j+1
	return render_template("viewfeedback.html",data=data)
@admin.route('/creatdiscussion',methods=['post','get'])
def creatdiscussion():
	data={}
	
	if 'submit' in request.form:
		title=request.form['Title']
		date=request.form['date']
		q="insert into discussionmaster values(null,'%s','%s')"%(title,date)
		insert(q)
	q="select * from discussionmaster"
	res=select(q)
	data['viewdisc']=res
	return render_template('creatdiscussion.html',data=data)

@admin.route('/viewblooddonors',methods=['post','get'])
def viewblooddonors():
	data={}
	q="select *,Concat(fname,' ',lname)as Name from donors"
	res=select(q)
	data['viewdonor']=res
	return render_template("viewblooddonors.html",data=data)
@admin.route('/participants',methods=['post','get'])
def participants():
	data={}
	id=request.args['id']
	q="select *,Concat(fname,' ',lname)as Name from discussiondetails inner join users using(userid) where master_id='%s'"%(id)
	res=select(q)
	data['viewpart']=res
	return render_template("viewparticipantions.html",data=data)



@admin.route('/viewdietrequest',methods=['post','get'])
def viewdietrequest():
	data={}
	
	q="SELECT * FROM `dietsrequest`  inner join  users using (userid)"
	res=select(q)
	data['viewfood']=res


	if "action" in request.args:
		action=request.args['action']
		did=request.args['did']


	else:
		action=None

	if action=='accept':
		q="update dietsrequest set status='accept'  where dietsrequest_id='%s'"%(did)
		update(q)
		return redirect(url_for('admin.viewdietrequest'))
	if action=='reject':
		q="update dietsrequest set status='reject'  where dietsrequest_id='%s'"%(did)
		update(q)
		return redirect(url_for('admin.viewdietrequest'))
	return render_template("viewdietrequest.html",data=data)


@admin.route('/viewexiserequest',methods=['post','get'])
def viewexiserequest():
	data={}
	
	q="SELECT * FROM `excersicesrequest`  inner join  users using (userid) "
	res=select(q)
	data['viewfood']=res


	if "action" in request.args:
		action=request.args['action']
		did=request.args['rid']


	else:
		action=None

	if action=='accept':
		q="update excersicesrequest set status='accept'  where excersicesrequest_id='%s'"%(did)
		update(q)
		return redirect(url_for('admin.viewexiserequest'))
	if action=='reject':
		q="update excersicesrequest set status='reject'  where excersicesrequest_id='%s'"%(did)
		update(q)
		return redirect(url_for('admin.viewexiserequest'))
	return render_template("viewexiserequest.html",data=data)





@admin.route('/viewfoodintake',methods=['post','get'])
def viewfoodintake():
	data={}
	id=request.args['id']
	q="SELECT * FROM `food`  INNER JOIN `foodintakes`  USING (`in_id`)  INNER JOIN `users` ON users.`userid`=`food`.`user_id`  where userid='%s'"%(id)
	res=select(q)
	data['viewfood']=res
	return render_template("viewfoodintake.html",data=data)

@admin.route('/suggest',methods=['post','get'])
def suggest():
	data={}
	id=request.args['did']
	if 'submit' in request.form:
		sug=request.form['sug']
		q="insert into dietsuggested values(null,'%s','%s',curdate())"%(id,sug)
		insert(q)
	q="select * from  dietsuggested where dietsrequest_id='%s'"%(id)
	res=select(q)
	data['viewsug']=res
	return render_template("adminsuggest.html",data=data,id=id)


@admin.route('/admin_addwalkingdetails',methods=['post','get'])
def admin_addwalkingdetails():
	data={}
	id=request.args['id']
	q="select * from walkingdetails  inner join users using (userid)  where userid='%s'"%(id)
	res=select(q)
	data['viewfeed']=res
	
	if 'submit' in request.form:
		step=request.form['step']
		name=request.form['name']
		id=request.args['id']
		q="insert into walkingdetails values(null,'%s','%s','%s')"%(id,step,name)
		insert(q)
		return redirect(url_for('admin.admin_addwalkingdetails',id=id)) 	
		
	return render_template("admin_addwalkingdetails.html",data=data)


@admin.route('/admin_view',methods=['post','get'])
def admin_view():
	data={}
	rid=request.args['rid']
	q="select * from excersice  where excersicesrequest_id='%s'"%(rid)
	res=select(q)
	data['viewfeed']=res
	
	if 'submit' in request.form:
		img=request.files['img']
		path="static/img"+str(uuid.uuid4())+img.filename
		img.save(path)
		
		
		q="insert into excersice values(null,'%s','%s','%s',curdate())"%(rid,session['healthagent_id'],path)
		insert(q)
		return redirect(url_for('admin.admin_view',rid=rid)) 	
		
	return render_template("admin_view.html",data=data)

@admin.route('/adminviewfoodintake',methods=['post','get'])
def adminviewfoodintake():
	data={}
	
	q="select * from foodintakes "
	res=select(q)
	data['viewfeed']=res
	
	if 'submit' in request.form:
		food=request.form['food']
		qty=request.form['qty']
		time=request.form['time']
		cal=request.form['cal']
		
		
		q="insert into foodintakes values(null,'%s','%s','%s','%s')"%(food,qty,time,cal)
		insert(q)
		return redirect(url_for('admin.adminviewfoodintake')) 	
		
	return render_template("adminviewfoodintake.html",data=data)




@admin.route('/employee_chat_customer',methods=['get','post'])
def employee_chat_customer():
	data={}
	receiver_id=request.args['receiver_id']
	sender_id=session['healthagent_id']
	# data['bank']=sender_id

	q="select * from chat where (sender_id='%s' and receiver_id='%s') or (receiver_id='%s' and sender_id='%s')"%(sender_id,receiver_id,sender_id,receiver_id)
	print(q)
	res1=select(q)
	data['msg']=res1

	
	if 'submit' in request.form:
		msg=request.form['message']
		q="insert into chat values(null,'%s','%s','%s',curdate())"%(sender_id,receiver_id,msg)
		insert(q)
		flash('send message')
		return redirect(url_for('admin.employee_chat_customer',receiver_id=receiver_id))

	return render_template("employee_chat_customer.html",data=data)



