require "class"

camera = class:new()

function camera:init(virt_w, virt_h, real_w, real_h)
	self.virtual_w = virt_w
	self.virtual_h = virt_h
	self.real_w = real_w
	self.real_h = real_h
end

function camera:get_virtual_scale()
	return
		self.real_w / self.virtual_w,
		self.real_h / self.virtual_h
end

function camera:get_virtual_scale_inv()
	return
		self.virtual_w / self.real_w,
		self.virtual_h / self.real_h
end