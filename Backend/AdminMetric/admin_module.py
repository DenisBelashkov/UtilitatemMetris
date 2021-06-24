from flask import render_template, request, redirect
import random
from module import Module

def generate_random_string(length):
	letters = '1234567890'
	rand_string = ''.join(random.choice(letters) for i in range(length))
	return rand_string

class AdminModule(Module):
	def __init__(self):
		pass

	def setOptions(self, app, db, base):
		Flat = base.classes.Personal_account
		Payment = base.classes.Payment_history
		Metrics = base.classes.Metrics
		Tariff = base.classes.Tariff
		Type_metric = base.classes.Type_metrics
		Users = base.classes.Users
		Address = base.classes.Address
		Type_address = base.classes.Type_address

		@app.route('/admin/users', methods=["GET"])
		def admin_users():
			users = db.session.query(Users).all()
			return render_template('users.html', users=users)

		# @app.route('/admin/newFlat', methods=["GET"])
		# def admin_address():
		# 	types = db.session.query(Type_address).all()
		# 	houses = db.session.query(Address).join(Type_address, Type_address.id_type_address == Address.id_type_address).filter(Type_address.name == "дом").all()
		# 	streets = db.session.query(Address).join(Type_address, Type_address.id_type_address == Address.id_type_address).filter(Type_address.name == "улица").all()
		# 	cities = db.session.query(Address).join(Type_address, Type_address.id_type_address == Address.id_type_address).filter(Type_address.name == "город").all()
		# 	return render_template('a')

		@app.route('/admin/metrics', methods=['GET'])
		def admin_metrics():
			metrics = db.session.query(Metrics, Tariff.price, Type_metric.name, Tariff.name.label('tariff'))\
				.join(Tariff, Tariff.id_tariff == Metrics.id_tariff)\
				.join(Type_metric, Type_metric.id_type == Metrics.id_type)\
				.all()
			return render_template('metrics.html', metrics=metrics)

		@app.route('/admin/flats', methods=['GET'])
		def admin_flats():
			flat = db.session.query(Flat, Address, Users.email)\
				.join(Address, Address.id_address == Flat.id_address)\
				.join(Users, Users.id_user == Flat.id_owner_user)\
				.all()
			return render_template('flats.html', flats=flat)

		@app.route('/admin/showMetrics/<flat_id>')
		def admin_show_metric(flat_id):
			metrics = db.session.query(Metrics, Tariff.price, Type_metric.name, Tariff.name.label('tariff'))\
				.join(Tariff, Tariff.id_tariff == Metrics.id_tariff)\
				.join(Type_metric, Type_metric.id_type == Metrics.id_type).filter(Metrics.id_personal_account == flat_id)\
				.all()
			return render_template('show_metric.html', metrics=metrics, flat=flat_id)

		@app.route('/admin/tariffs', methods=['GET'])
		def admin_tariffs():
			tariff = db.session.query(Tariff, Type_metric.name)\
				.join(Type_metric, Type_metric.id_type == Tariff.id_type)\
				.all()
			return render_template('tariffs.html', tariffs=tariff)

		@app.route('/admin/edit_user', methods=['POST'])
		def admin_edit_user():
			id = request.form['id']
			password = request.form['password']
			name = request.form['name']
			user = db.session.query(Users).filter(Users.id_user == id).first()
			user.password = password
			user.email = name
			db.session.commit()
			return redirect('/admin/users')

		@app.route('/admin/edit_metric', methods=['POST'])
		def admin_edit_metric():
			form = request.form
			id_new = request.form['identifier']
			flat = request.form['flat']
			id_old = request.form['id_metric']
			curr = request.form['curr_value']
			prev = request.form['prev_value']

			metric = db.session.query(Metrics).filter(Metrics.identifier == id_new).one_or_none()
			if metric:
				return redirect('/admin/showMetrics/' + str(flat))
			metric = db.session.query(Metrics).filter(Metrics.identifier == id_old).one_or_none()
			if metric is None:
				return redirect('/admin/showMetrics/' + str(flat))
			metric.identifier = id_new
			metric.curr_value = curr
			metric.prev_value = prev
			db.session.commit()
			return redirect('/admin/showMetrics/' + str(flat))

		@app.route('/admin/edit_owner', methods=['GET'])
		def admin_edit_owner():
			id_user = request.args.get('id_user')
			flat = request.args.get('flat')
			users = db.session.query(Users).filter(Users.demo == 1).filter(Users.id_user != id_user).all()
			return render_template('edit_owner.html', users=users, flat=flat)

		@app.route('/admin/save_owner', methods=['GET'])
		def admin_save_owner():
			id_user = request.args.get('id_user')
			flat = request.args.get('flat')
			flat_ = db.session.query(Flat).filter(Flat.id_personal_account == flat).first()
			flat_.id_owner_user = id_user
			db.session.commit()
			return redirect('/admin/flats')

		@app.route('/admin/edit_flat', methods=['POST'])
		def admin_edit_flat():
			flat_id = request.form['flat']
			new_flat = request.form['new_flat']
			flat = db.session.query(Flat).filter(Flat.id_personal_account == new_flat).one_or_none()
			if flat is None:
				flat = db.session.query(Flat).filter(Flat.id_personal_account == flat_id).first()
				flat_new = Flat()
				flat_new.id_personal_account = new_flat
				flat_new.id_address = flat.id_address
				flat_new.id_owner_user = flat.id_owner_user
				db.session.add(flat_new)
				db.session.commit()
				db.session.query(Metrics).filter(Metrics.id_personal_account == flat_id).update({Metrics.id_personal_account: new_flat })
				db.session.delete(flat)
				db.session.commit()
			return redirect('/admin/flats')

		@app.route('/admin/create_flat/city', methods=['GET'])
		def admin_create_flat():
			cities = db.session.query(Address).join(Type_address, Type_address.id_type_adress == Address.type).filter(Type_address.name == "город").all()
			return render_template('create_flat.html', cities=cities)

		@app.route('/admin/create_flat/street', methods=['POST'])
		def admin_create_flat_street():
			city = request.form['city']
			cities = db.session.query(Address)\
				.join(Type_address, Type_address.id_type_adress == Address.type)\
				.filter(Type_address.name == "город")\
				.all()
			streets = db.session.query(Address)\
				.filter(Address.id_include == city)\
				.all()
			return render_template('create_flat.html', cities=cities, streets=streets, select_city=int(city))

		@app.route('/admin/create_flat/house', methods=['POST'])
		def admin_create_flat_house():
			street = request.form['street']
			city = request.form['city_old']
			cities = db.session.query(Address) \
				.join(Type_address, Type_address.id_type_adress == Address.type) \
				.filter(Type_address.name == "город") \
				.all()
			streets = db.session.query(Address)\
				.filter(Address.id_include == city)\
				.all()
			houses = db.session.query(Address)\
				.filter(Address.id_include == street) \
				.all()
			return render_template('create_flat.html', cities=cities, streets=streets, houses=houses, select_city=int(city), select_street=int(street))

		@app.route('/admin/create_flat/flat', methods=['POST'])
		def admin_create_flat_flat():
			street = request.form['street_house']
			city = request.form['city_house']
			house = request.form['house']
			cities = db.session.query(Address) \
				.join(Type_address, Type_address.id_type_adress == Address.type) \
				.filter(Type_address.name == "город") \
				.all()
			streets = db.session.query(Address) \
				.filter(Address.id_include == city) \
				.all()
			houses = db.session.query(Address) \
				.filter(Address.id_include == street) \
				.all()
			return render_template('create_flat.html', cities=cities, streets=streets, houses=houses, select_city=int(city), select_street=int(street), select_house=int(house))

		@app.route('/admin/add_street', methods=['POST'])
		def admin_add_street():
			city = request.form['s_city']
			name = request.form['new_street']
			address = db.session.query(Address).filter(Address.id_include == city).filter(Address.name == name).one_or_none()
			if address is None:
				address = Address()
				type = db.session.query(Type_address).filter(Type_address.name == 'улица').first()
				address.id_include = city
				address.name = name
				address.type = type.id_type_adress
				db.session.add(address)
				db.session.commit()
			return redirect('/admin/create_flat/city')

		@app.route('/admin/add_house', methods=['POST'])
		def admin_add_house():
			street = request.form['parent_street']
			name = request.form['new_house']
			address = db.session.query(Address).filter(Address.id_include == street).filter(Address.name == name).one_or_none()
			if address is None:
				address = Address()
				type = db.session.query(Type_address).filter(Type_address.name == 'дом').first()
				address.id_include = street
				address.name = name
				address.type = type.id_type_adress
				db.session.add(address)
				db.session.commit()
			return redirect('/admin/create_flat/city')

		@app.route('/admin/choice_house', methods=['POST'])
		def admin_choice_house():
			house = request.form['select_street']
			flat = request.form['select_house']
			address = db.session.query(Address).filter(Address.id_include == house).filter(Address.name == flat).one_or_none()
			if address is None:
				users = db.session.query(Users).filter(Users.demo == 1).all()
				return render_template('new_flat.html', users=users, address=flat, house=house)
			return redirect('/admin/create_flat/city')

		@app.route('/admin/save_flat', methods=['GET'])
		def admin_save_new_owner():
			flat_name = request.args['address']
			house = request.args['house']
			user = request.args['id_user']
			address = Address()
			type = db.session.query(Type_address).filter(Type_address.name == 'дом').first()
			address.id_include = house
			address.name = flat_name
			address.type = type.id_type_adress
			db.session.add(address)
			db.session.commit()
			flat = Flat()
			flat.id_owner_user = user
			flat.id_address = address.id_address
			flat.id_personal_account = generate_random_string(10)
			db.session.add(flat)
			db.session.commit()
			return redirect('/admin/flats')