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
				namespace example3
				{
					///
					/// <summary> * ΢�������࣬���������ʵ���ߣ���Commandģʽ�г䵱Receiver </summary>
					/// 
					class MsiMainBoard : public MainBoardApi
					{
					///	
					///	 <summary> * �����Ŀ��������ʵ�� </summary>
					///	 
					public:
						virtual void open();
					///	
					///	 <summary> * �����������������������ʵ�� </summary>
					///	 
						virtual void reset();
					};
				}
			}
		}
	}
}