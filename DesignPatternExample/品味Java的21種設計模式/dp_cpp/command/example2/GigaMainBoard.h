#pragma once

#include "MainBoardApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example2
				{
					///
					/// <summary> * ���������࣬�������������ʵ���ߣ���Commandģʽ�г䵱Receiver </summary>
					/// 
					class GigaMainBoard : public MainBoardApi
					{
					///	
					///	 <summary> * �����Ŀ��������ʵ�� </summary>
					///	 
					public:
						virtual void open();
					};
				}
			}
		}
	}
}