#pragma once

#include "CookApi.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example6
				{
					///
					/// <summary> * ����ӿڣ�����ִ�еĲ��� </summary>
					/// 
					class Command
					{
					///	
					///	 <summary> * ִ�������Ӧ�Ĳ��� </summary>
					///	 
					public:
						virtual public void execute() = 0;
					///	
					///	 <summary> * ��������Ľ����� </summary>
					///	 * <param name="cookApi"> ����Ľ�����  </param>
					///	 
						virtual public void setCookApi(CookApi*) = 0;
					///	
					///	 <summary> * ���ط�����������ţ����ǵ�˵����� </summary>
					///	 * <returns> ������������� </returns>
					///	 
						virtual public int getTableNum() = 0;
					};
				}
			}
		}
	}
}