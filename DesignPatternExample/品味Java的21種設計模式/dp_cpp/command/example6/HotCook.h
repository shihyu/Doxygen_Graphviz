#pragma once

#include "CookApi.h"
#include "Command.h"
#include "CommandQueue.h"
#include <string>

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
					/// <summary> * ��ʦ�������Ȳ˵ĳ�ʦ </summary>
					/// 
					class HotCook : public CookApi, Runnable
					{
					///	
					///	 <summary> * ��ʦ���� </summary>
					///	 
					private:
						std::string name;
					///	
					///	 <summary> * ���췽���������ʦ���� </summary>
					///	 * <param name="name"> ��ʦ���� </param>
					///	 
					public:
						HotCook(std::string name);

						virtual void cook(int tableNum, std::string name);

						virtual void run();
					};
				}
			}
		}
	}
}