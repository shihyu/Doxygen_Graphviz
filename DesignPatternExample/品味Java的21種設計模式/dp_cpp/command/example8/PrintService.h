#pragma once

#include "Command.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace command
			{
				namespace example8
				{

					class PrintService : public Command
					{
					///	
					///	 <summary> * Ҫ��������� </summary>
					///	 
					private:
						std::string str;
					///	
					///	 <summary> * ���췽��������Ҫ��������� </summary>
					///	 * <param name="s"> Ҫ��������� </param>
					///	 
					public:
						PrintService(std::string s);

						virtual void execute();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								str = "";

								initialized = true;
							}
						}
					};
				}
			}
		}
	}
}