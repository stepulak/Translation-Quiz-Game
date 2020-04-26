require "class"

camera = class:new()

function camera:init(virt_w, virt_h, real_w, real_h)
	self.virtual = {
		width = virt_w,
		height = virt_h
	}
	self.real = {
		width = real_w,
		height = real_h
	}
end

function camera:get_virtual_scale()
	return
		self.real.width/self.virtual.width, 
		self.real.height/self.virtual.height
end