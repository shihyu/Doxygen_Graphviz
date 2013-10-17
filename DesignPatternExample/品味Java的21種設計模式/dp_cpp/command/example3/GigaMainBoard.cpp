#include "GigaMainBoard.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example3
				{

					void GigaMainBoard::open()
					{
						puts("技嘉主板现在正在开机，请等候");
						puts("接通电源......");
						puts("设备检查......");
						puts("装载系统......");
						puts("机器正常运转起来......");
						puts("机器已经正常打开，请操作");
					}

					void GigaMainBoard::reset()
					{
						puts("技嘉主板现在正在重新启动机器，请等候");
						puts("机器已经正常打开，请操作");
					}
				}
			}
		}
	}
}
