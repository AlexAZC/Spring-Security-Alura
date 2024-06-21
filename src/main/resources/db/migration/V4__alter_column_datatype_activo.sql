alter table medicos
alter column activo type boolean
using case when activo = 0 then false
when activo = 1 then true
else null
end;
