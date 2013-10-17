#include "MSIMainboard.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example4
				{

					MSIMainboard::MSIMainboard(int cpuHoles)
					{
						this->cpuHoles = cpuHoles;
					}

					void MSIMainboard::installCPU()
					{
						puts("now in MSIMainboard,cpuHoles="+cpuHoles);
					}
				}
			}
		}
	}
}
